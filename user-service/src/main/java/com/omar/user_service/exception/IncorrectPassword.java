package com.omar.user_service.exception;

public class IncorrectPassword extends RuntimeException {
    public IncorrectPassword(String message) {
        super(message);
    }
}
