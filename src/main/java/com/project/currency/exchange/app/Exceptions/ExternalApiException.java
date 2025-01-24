package com.project.currency.exchange.app.Exceptions;

public class ExternalApiException extends RuntimeException {
  public ExternalApiException(String message) {
    super(message);
  }
}
