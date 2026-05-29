package com.omar.question_service.dto;

public record ErrorRes(
        String message,
        int statusCode
) {
}
