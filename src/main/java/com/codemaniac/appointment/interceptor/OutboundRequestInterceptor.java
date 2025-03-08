package com.codemaniac.appointment.interceptor;

import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class OutboundRequestInterceptor implements ClientHttpRequestInterceptor {

  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
    HttpHeaders headers = request.getHeaders();

    generateRequestHeaders(headers);
    return execution.execute(request, body);
  }

  private void generateRequestHeaders(HttpHeaders headers) {
    headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
  }
}
