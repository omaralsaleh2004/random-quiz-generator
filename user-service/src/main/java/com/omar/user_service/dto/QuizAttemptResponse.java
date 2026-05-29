package com.omar.user_service.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuizAttemptResponse {
    private Integer quizId;
    private String quizTitle;
    private Integer userId;
    private String username;
    private Integer score;
    private Integer total;
    private ResultStatus resultStatus;
    private LocalDateTime submittedAt;
}
