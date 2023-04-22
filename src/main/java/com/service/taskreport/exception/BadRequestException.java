package com.service.taskreport.exception;

public abstract class BadRequestException extends Exception {
  public BadRequestException(String message) {
    super(message);
  }
}
