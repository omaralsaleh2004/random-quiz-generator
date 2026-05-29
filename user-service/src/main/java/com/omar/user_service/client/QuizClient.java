package com.omar.user_service.client;

import com.omar.user_service.dto.QuizAttemptResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "quiz-service")
public interface QuizClient {

    @GetMapping("/quiz/attempt/{quizId}")
    QuizAttemptResponse getUserAttempt(
            @RequestHeader("X-User-Id") int userId,
            @PathVariable int quizId
    );
}
