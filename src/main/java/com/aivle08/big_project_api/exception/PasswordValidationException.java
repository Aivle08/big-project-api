package com.aivle08.big_project_api.exception;

import org.springframework.http.HttpStatus;

public class PasswordValidationException extends RuntimeException {
    private final HttpStatus status;

    public PasswordValidationException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
