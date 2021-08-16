package com.cm.batch.report.listener;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class DocumentGenerationException extends RuntimeException {
  private static final long serialVersionUID = 5760599181199136653L;
  private final String message;

  public DocumentGenerationException(String message) {
    this.message = message;
  }
}
