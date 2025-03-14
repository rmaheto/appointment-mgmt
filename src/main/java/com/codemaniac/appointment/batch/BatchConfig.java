package com.codemaniac.appointment.batch;

import jakarta.annotation.Nonnull;
import javax.sql.DataSource;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfig extends DefaultBatchConfiguration {

  @Value("${DB_USERNAME}")
  private String dbUsername;

  @Value("${DB_PASSWORD.key}")
  private String dbPassword;

  @Value("${spring.datasource.url}")
  private String dbUrl;

  @Value("${spring.datasource.driver-class-name}")
  private String dbDriver;

  @Override
  @Nonnull
  public DataSource getDataSource() {

    final DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setUrl(dbUrl);
    dataSource.setUsername(dbUsername);
    dataSource.setPassword(dbPassword);
    dataSource.setDriverClassName(dbDriver);
    return dataSource;
  }
}
