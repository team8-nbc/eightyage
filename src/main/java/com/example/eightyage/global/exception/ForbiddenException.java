package com.example.eightyage.global.exception;

public class ForbiddenException extends CustomException {

    public ForbiddenException() {
        super(ErrorCode.FORBIDDEN);
    }

    public ForbiddenException(String message) {
        super(ErrorCode.FORBIDDEN,message);
    }
}
