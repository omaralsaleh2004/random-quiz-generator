package com.omar.quiz_service.controller;


import com.omar.quiz_service.dto.*;
import com.omar.quiz_service.model.Quiz;
import com.omar.quiz_service.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<List<QuizResponse>>> getQuiz(@RequestHeader("X-User-Id") Integer userId,@PathVariable int id) {
        return quizService.getQuiz(userId,id);
    }

    @PostMapping("/submit/{id}")
    public ResponseEntity<ApiResponse<QuizAttemptResponse>> submitQuiz (@RequestHeader("X-User-Id")Integer userId , @PathVariable int id , @RequestBody List<QuizSubmitRes> quizSubmitRes) {
       return quizService.calculateResult(userId ,id , quizSubmitRes);
    }

    @GetMapping("/attempt/{quizId}")
    public QuizAttemptResponse getUserAttempt(@RequestHeader("X-User-Id")int userId,@PathVariable int quizId) {
        return quizService.getUserAttempt(userId , quizId);
    }

    @GetMapping("/attempt")
    public ResponseEntity<List<QuizAttemptResponse>> getAllUserAttempts(@RequestHeader("X-User-Id") Integer userId) {
        return quizService.getAllUserAttempt(userId);
    }
}
