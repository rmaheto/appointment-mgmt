package com.codemaniac.appointment.config;

import com.codemaniac.appointment.interceptor.OutboundRequestInterceptor;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class RestTemplateConfig {

  private final OutboundRequestInterceptor outboundRequestInterceptor;

  @Bean
  public RestTemplate restTemplate() {
    final RestTemplate restTemplate = createNewRestTemplate();
    restTemplate.getInterceptors().add(outboundRequestInterceptor);

    final MappingJackson2HttpMessageConverter jsonMessageConverter =
        new MappingJackson2HttpMessageConverter();
    jsonMessageConverter.setObjectMapper(buildObjectMapper());

    final List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
    messageConverters.add(jsonMessageConverter);
    messageConverters.add(new StringHttpMessageConverter());

    restTemplate.setMessageConverters(messageConverters);
    return restTemplate;
  }

  private RestTemplate createNewRestTemplate() {

    // Configure Connection Manager
    final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
    connectionManager.setMaxTotal(200); // Max total connections
    connectionManager.setDefaultMaxPerRoute(100); // Max per route

    // New Way to Configure Timeouts in Apache HttpClient 5.2+
    final ConnectionConfig connConfig =
        ConnectionConfig.custom()
            .setConnectTimeout(10_000, TimeUnit.MILLISECONDS) // 10 sec connection timeout
            .setSocketTimeout(40_000, TimeUnit.MILLISECONDS) // 40 sec socket timeout
            .build();

    connectionManager.setDefaultConnectionConfig(connConfig);

    final CloseableHttpClient httpClient =
        HttpClients.custom()
            .setConnectionManager(connectionManager)
            .disableAutomaticRetries() // Disable default retries
            .disableCookieManagement() // Disable cookies
            .build();

    return new RestTemplate(
        new BufferingClientHttpRequestFactory(
            new HttpComponentsClientHttpRequestFactory(httpClient)));
  }

  private ObjectMapper buildObjectMapper() {
    final ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(Include.NON_NULL);
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.registerModule(new JsonNullableModule());
    mapper.registerModule(new JavaTimeModule());

    return mapper;
  }
}
