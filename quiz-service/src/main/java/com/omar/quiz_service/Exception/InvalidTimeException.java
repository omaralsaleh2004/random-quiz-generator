package com.omar.quiz_service.Exception;

public class InvalidTimeException extends RuntimeException {
    public InvalidTimeException(String message) {
        super(message);
    }
}
