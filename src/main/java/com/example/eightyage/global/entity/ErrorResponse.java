package com.example.eightyage.global.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {

    private String status;

    private Integer code;

    private String message;

    public ErrorResponse(HttpStatus httpStatus, String message) {
        this.status = httpStatus.name();
        this.code = httpStatus.value();
        this.message = message;
    }

    public static ErrorResponse of(HttpStatus httpStatus, String message) {
        return new ErrorResponse(httpStatus, message);
    }
}
