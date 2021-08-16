package com.cm.batch.report.remote.exception;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class RemoteServerException extends RuntimeException {
  private static final long serialVersionUID = -6403147552464795632L;
  private final String message;

  public RemoteServerException(String message) {
    this.message = message;
  }
}
