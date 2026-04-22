package com.app.auth.auth_app_backend.Exception;

import com.app.auth.auth_app_backend.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<ErrorResponse> handleRResourceNotFoundException(ResourceNotFoundException exception){
    ErrorResponse internalServerError = new ErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND,"Internal Server Error");
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(internalServerError);
}

@ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse>  handleMissingInformation(IllegalArgumentException exception){
        ErrorResponse missingInformation = new ErrorResponse(exception.getMessage(),HttpStatus.BAD_REQUEST,"Enter All The Fields");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(missingInformation);
}
}
