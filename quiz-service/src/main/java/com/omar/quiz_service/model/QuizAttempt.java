package com.omar.quiz_service.model;

import com.omar.quiz_service.dto.ResultStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class QuizAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer quizId;
    private Integer userId;
    private Integer score;
    private Integer total;
    @Enumerated(EnumType.STRING)
    private ResultStatus resultStatus;
    private LocalDateTime submittedAt;
}
