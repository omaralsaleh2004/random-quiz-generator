package com.omar.user_service.dto;

public record ErrorRes(
        String message,
        int statusCode
) {
}
