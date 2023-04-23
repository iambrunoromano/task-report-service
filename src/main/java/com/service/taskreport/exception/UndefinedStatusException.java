package com.service.taskreport.exception;

public class UndefinedStatusException extends BadRequestException {
  public UndefinedStatusException(String message) {
    super(message);
  }
}
