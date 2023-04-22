package com.service.taskreport.exception;

public abstract class NotFoundException extends Exception {
  public NotFoundException(String message) {
    super(message);
  }
}
