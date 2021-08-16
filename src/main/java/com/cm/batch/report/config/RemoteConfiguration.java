package com.cm.batch.report.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/** @author chandresh.mishra */
@Configuration
@EnableCaching
public class RemoteConfiguration {

  @Bean
  public RestTemplate restTemplate(@Value("${remote.client.url}") String baseURL) {
    RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
    return restTemplateBuilder
        .errorHandler(new RestTemplateResponseErrorHandler())
        .rootUri(baseURL)
        .build();
  }
}
