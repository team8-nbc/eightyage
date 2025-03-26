package com.example.eightyage.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.example.eightyage.global.exception.ErrorMessage.*;

@Getter
public enum ErrorCode {
    AUTHORIZATION(HttpStatus.UNAUTHORIZED, DEFAULT_UNAUTHORIZED.getMessage()),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, DEFAULT_BAD_REQUEST.getMessage()),
    NOT_FOUND(HttpStatus.NOT_FOUND, DEFAULT_NOT_FOUND.getMessage()),
    FORBIDDEN(HttpStatus.FORBIDDEN, DEFAULT_FORBIDDEN.getMessage());

    private final HttpStatus status;
    private final String defaultMessage;

    ErrorCode(HttpStatus status, String defaultMessage) {
        this.status = status;
        this.defaultMessage = defaultMessage;
    }
}
