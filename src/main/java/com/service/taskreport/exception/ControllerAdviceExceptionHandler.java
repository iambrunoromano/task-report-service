package com.service.taskreport.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerAdviceExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = {NotFoundException.class})
  protected ResponseEntity<Object> handleNotFoundException(
      NotFoundException notFoundException, WebRequest request) {
    return handleExceptionInternal(
        notFoundException,
        notFoundException.getMessage(),
        new HttpHeaders(),
        HttpStatus.NOT_FOUND,
        request);
  }

  @ExceptionHandler(value = {BadRequestException.class})
  protected ResponseEntity<Object> handleBadRequestException(
      BadRequestException badRequestException, WebRequest request) {
    return handleExceptionInternal(
        badRequestException,
        badRequestException.getMessage(),
        new HttpHeaders(),
        HttpStatus.BAD_REQUEST,
        request);
  }
}
