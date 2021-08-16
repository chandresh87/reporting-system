package com.cm.batch.report.remote;

import com.cm.batch.report.config.RestTemplateResponseErrorHandler;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@AutoConfigureWireMock(port = 0)
class CurrencyRemoteServiceImplTest {

  @Autowired private WireMockServer wireMockServer;
  private CurrencyRemoteService currencyRemoteService;

  @BeforeEach
  void setup() {
    RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
    RestTemplate restTemplate =
        restTemplateBuilder
            .errorHandler(new RestTemplateResponseErrorHandler())
            .rootUri(wireMockServer.baseUrl())
            .build();
    currencyRemoteService = new CurrencyRemoteServiceImpl(restTemplate, "12345");
  }

  @Test
  void getCurrencyRates() {

    stubFor(
        get(urlEqualTo("/v1/latest?access_key=12345"))
            .willReturn(
                aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBodyFile("/rates.json")));

    CurrencyRatesDTO currencyRates = currencyRemoteService.getCurrencyRates("EUR");
    assertNotNull(currencyRates);
    assertEquals(0.846924, currencyRates.getRates().get("GBP").doubleValue());
  }
}
