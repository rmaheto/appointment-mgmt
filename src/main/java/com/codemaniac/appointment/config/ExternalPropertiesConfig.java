package com.codemaniac.appointment.config;

import java.io.File;
import java.nio.file.Paths;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;

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
      configurer.setLocation(new FileSystemResource(externalFile));
    } else {
      log.warn(
          "External properties file not found: {}. Using internal properties.",
          externalFilePath);
    }

    configurer.setIgnoreResourceNotFound(true);
    configurer.setIgnoreUnresolvablePlaceholders(true);
    return configurer;
  }
}
