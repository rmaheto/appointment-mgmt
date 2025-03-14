package com.codemaniac.appointment.config;

import com.codemaniac.appointment.Util.EncryptionUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

@Configuration
@Slf4j
public class ExternalPropertiesConfig {

  @Bean
  public static PropertySourcesPlaceholderConfigurer propertyConfigurer(
      final Environment environment) {

    final PropertySourcesPlaceholderConfigurer configurer =
        new PropertySourcesPlaceholderConfigurer();

    final String activeProfile =
        Optional.of(environment.getActiveProfiles())
            .filter(profiles -> profiles.length > 0)
            .map(profiles -> profiles[0])
            .orElse("default");

    final String externalFilePath =
        Paths.get(File.separator + "keys", "appt_mgmt_credentials_" + activeProfile + ".properties")
            .toAbsolutePath()
            .toString();

    final File externalFile = new File(externalFilePath);

    if (externalFile.exists()) {
      log.info("Loading external properties from: {}", externalFilePath);

      try {
        final Properties properties = new Properties();
        properties.load(new FileInputStream(externalFile));

        boolean updated = false;

        // Iterate over properties and check for unencrypted values
        for (final String key : properties.stringPropertyNames()) {
          final String value = properties.getProperty(key);

          if (key.endsWith(".key") && !EncryptionUtil.isEncrypted(value)) {
            // Encrypt the value and update properties
            final String encryptedValue = EncryptionUtil.encrypt(value);
            properties.setProperty(key, encryptedValue);
            updated = true;
            log.info("🔒 Encrypting property: {}", key);
          }
        }

        // Save updated properties if changes were made
        if (updated) {
          try (final FileOutputStream outputStream = new FileOutputStream(externalFile)) {
            properties.store(outputStream, "Updated with encrypted values");
            log.info("Updated properties file with encrypted values.");
          }
        }

        // Decrypt values before setting them in Spring
        final Properties decryptedProperties = new Properties();
        properties.forEach((key, value) -> {
          final String keyStr = key.toString();
          final String valueStr = value.toString();
          if (keyStr.endsWith(".key")) {
            decryptedProperties.setProperty(keyStr, EncryptionUtil.decrypt(valueStr));
          } else {
            decryptedProperties.setProperty(keyStr, valueStr);
          }
        });

        configurer.setProperties(decryptedProperties);

      } catch (final IOException e) {
        log.error("Failed to load external properties: {}", e.getMessage());
      }
    } else {
      log.warn("External properties file not found: {}. Using internal properties.", externalFilePath);
    }

    configurer.setIgnoreResourceNotFound(true);
    configurer.setIgnoreUnresolvablePlaceholders(true);
    return configurer;
  }
}
