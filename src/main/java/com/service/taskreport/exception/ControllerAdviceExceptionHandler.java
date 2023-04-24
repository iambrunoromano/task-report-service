package com.service.taskreport.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

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

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    List<DetailError> detailErrorList = new ArrayList<>();
    for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
      detailErrorList.add(new DetailError(fieldError.getField(), fieldError.getDefaultMessage()));
    }
    ExceptionResponse exceptionResponse =
        new ExceptionResponse("Field error", "field_error", detailErrorList, ZonedDateTime.now());
    return handleExceptionInternal(
        ex, exceptionResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }
}
