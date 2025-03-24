package com.example.eightyage.global.exception;

import com.example.eightyage.global.entity.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ErrorResponse invalidRequestExceptionException(CustomException ex) {
        HttpStatus httpStatus = ex.getHttpStatus();
        return ErrorResponse.of(httpStatus, ex.getMessage());
    }
}
