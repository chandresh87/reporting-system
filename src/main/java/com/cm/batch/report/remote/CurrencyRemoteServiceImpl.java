package com.cm.batch.report.remote;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/** @author chandresh.mishra */
@Service
@Slf4j
public class CurrencyRemoteServiceImpl implements CurrencyRemoteService {

  private final RestTemplate restTemplate;
  private final String accessKey;
  private final String url = "/v1/latest";

  public CurrencyRemoteServiceImpl(
      RestTemplate restTemplate, @Value("${remote.client.accessKey}") String accessKey) {
    this.restTemplate = restTemplate;
    this.accessKey = accessKey;
  }

  @Override
  @Cacheable(key = "#baseCurrency", sync = true, cacheNames = "rates")
  public CurrencyRatesDTO getCurrencyRates(String baseCurrency) {

    log.info("Calling getCurrencyRates service for base currency {}", baseCurrency);

    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

    UriComponentsBuilder builder =
        UriComponentsBuilder.fromPath(url).queryParam("access_key", accessKey);

    HttpEntity<?> entity = new HttpEntity<>(headers);

    HttpEntity<CurrencyRatesDTO> response =
        restTemplate.exchange(
            builder.toUriString(), HttpMethod.GET, entity, CurrencyRatesDTO.class);

    return response.getBody();
  }

  @CacheEvict(value = "rates", allEntries = true)
  public void evictAllCacheValues() {
    log.info("Evicting rates cache");
  }
}
