package com.omar.quiz_service.dto;

public record ApiResponse<T>(
        T data,
        String message
) {
}
