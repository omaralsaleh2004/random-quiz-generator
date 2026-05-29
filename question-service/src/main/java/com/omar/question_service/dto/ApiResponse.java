package com.omar.question_service.dto;

public record ApiResponse<T>(
        T data,
        String message
) {
}
