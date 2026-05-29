package com.omar.quiz_service.dto;

public record ErrorRes(
        String message,
        int statusCode
) {
}
