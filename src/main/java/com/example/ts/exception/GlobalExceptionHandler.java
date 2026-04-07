package com.example.ts.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorResponse handleValidation(MethodArgumentNotValidException ex) {
    log.error("Validation error: BAD_REQUEST (400)");

    String message = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(e -> e.getField() + ": " + e.getDefaultMessage())
        .collect(Collectors.joining(", "));

    return new ErrorResponse(
        LocalDateTime.now(),
        HttpStatus.BAD_REQUEST.value(),
        message
    );
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public ErrorResponse handleOther(Exception ex) {
    log.error("Internal server error: INTERNAL_SERVER_ERROR (500)");

    return new ErrorResponse(
        LocalDateTime.now(),
        500,
        "Unexpected error"
    );
  }

  @ExceptionHandler(AppException.class)
  @ResponseBody
  public ErrorResponse handleAppException(AppException ex) {
    log.error("Application error: status {}", ex.getStatus());

    return new ErrorResponse(
        LocalDateTime.now(),
        ex.getStatus(),
        ex.getMessage()
    );
  }
}