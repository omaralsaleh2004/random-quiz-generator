package com.omar.quiz_service.model;

import com.omar.quiz_service.dto.QuizStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private Integer creatorUserId;
    private LocalDateTime createdAt;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @ElementCollection
    private List<Integer> questions;
}
