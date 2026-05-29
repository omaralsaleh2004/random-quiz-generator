package com.omar.quiz_service.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateQuizRequest {

    private String title;
    private String category;
    private int numQ;
    private int durationMinutes;
    private LocalDateTime startTime;
}