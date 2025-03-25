package com.example.eightyage.global.exception;

import com.example.eightyage.global.entity.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ErrorResponse<String> invalidRequestExceptionException(CustomException ex) {
        HttpStatus httpStatus = ex.getHttpStatus();
        return ErrorResponse.of(httpStatus, ex.getMessage());
    }

    @ExceptionHandler
    public ErrorResponse<List<String>> handleValidationException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();

        List<String> validFailedList = fieldErrors.stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, validFailedList);
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse<String> handleGlobalException(Exception e) {
        log.error("Exception : {}",e.getMessage(),  e);
        return ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");
    }
}
