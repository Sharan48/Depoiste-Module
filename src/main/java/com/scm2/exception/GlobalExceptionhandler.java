package com.scm2.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionhandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorDetail> handleInternalServerError(Exception exception, WebRequest webRequest) {
        ErrorDetail errordatails = new ErrorDetail(new Date(),
                exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errordatails, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Map<String, String> errorDetails = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errorDetails.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler
    public ResponseEntity<ErrorDetail> handlePhoneNumberLareadyExists(ResourceNotFoundException ex) {
        ErrorDetail error = new ErrorDetail((ex.getMessage()));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler
    public ResponseEntity<ErrorDetail> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        ErrorDetail error = new ErrorDetail(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);

    }

    @ExceptionHandler
    public ResponseEntity<ErrorDetail> handleBlogException(BlogException ex,
            WebRequest webRequest) {
        ErrorDetail error = new ErrorDetail(new Date(), ex.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

    }

}
