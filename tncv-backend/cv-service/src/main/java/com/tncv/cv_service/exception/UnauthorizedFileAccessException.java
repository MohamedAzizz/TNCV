package com.tncv.cv_service.exception;

public class UnauthorizedFileAccessException extends RuntimeException {

    public UnauthorizedFileAccessException(String message) {
        super(message);
    }
}
