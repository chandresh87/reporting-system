package com.cm.batch.report.config;

import com.cm.batch.report.remote.exception.RemoteClientException;
import com.cm.batch.report.remote.exception.RemoteServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

/** @author chandresh.mishra */
@Slf4j
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

  @Override
  public boolean hasError(ClientHttpResponse httpResponse) throws IOException {

    return (httpResponse.getStatusCode().series() == CLIENT_ERROR
        || httpResponse.getStatusCode().series() == SERVER_ERROR);
  }

  @Override
  public void handleError(ClientHttpResponse httpResponse) throws IOException {

    log.error("Remote Exception {}", httpResponse.getBody());

    if (httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {
      throw new RemoteServerException("Remove server exception");
    } else if (httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
      throw new RemoteClientException("Remove client exception");
    }
  }
}
