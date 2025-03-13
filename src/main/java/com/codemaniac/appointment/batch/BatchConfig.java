package com.codemaniac.appointment.batch;

import jakarta.annotation.Nonnull;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@EnableBatchProcessing
@Slf4j
public class BatchConfig extends DefaultBatchConfiguration {

  @Value("${DB_USERNAME}")
  private String dbUsername;

  @Value("${DB_PASSWORD}")
  private String dbPassword;

  @Override
  @Nonnull
  public DataSource getDataSource() {
    log.info("🔐 Using custom DataSource for Spring Batch with user: {}", dbUsername);

    final DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setUrl("jdbc:mysql://localhost:3306/appointments_db");
    dataSource.setUsername(dbUsername);
    dataSource.setPassword(dbPassword);
    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
    return dataSource;
  }
}
