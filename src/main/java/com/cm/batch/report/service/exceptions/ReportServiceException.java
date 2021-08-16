package com.cm.batch.report.service.exceptions;

import lombok.Getter;
import lombok.ToString;

/** @author chandresh.mishra */
@ToString
@Getter
public class ReportServiceException extends RuntimeException {
  private static final long serialVersionUID = -417143438652052125L;
  private final String message;

  public ReportServiceException(String message) {
    this.message = message;
  }
}
