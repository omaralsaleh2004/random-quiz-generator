package com.omar.question_service.dto;


public record QuizResponse(
        int id,
        String questionTitle,
        String option1,
        String option2,
        String option3,
        String option4
) {
}
