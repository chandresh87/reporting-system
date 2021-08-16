package com.cm.batch.report.remote.exception;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class RemoteClientException extends RuntimeException {
  private static final long serialVersionUID = 5760599181199136653L;
  private final String message;

  public RemoteClientException(String message) {
    this.message = message;
  }
}
