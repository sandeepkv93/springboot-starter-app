package com.example.auth.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
    ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), null);
    return new ResponseEntity<>(errorResponse, ex.getStatus());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> validationErrors = new HashMap<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            error -> {
              String fieldName =
                  error instanceof FieldError fielderror
                      ? fielderror.getField()
                      : error.getObjectName();
              String errorMessage = error.getDefaultMessage();
              validationErrors.put(fieldName, errorMessage);
            });

    ErrorResponse errorResponse = new ErrorResponse("Validation failed", validationErrors);
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  private record ErrorResponse(String message, Map<String, String> errors) {}
}
