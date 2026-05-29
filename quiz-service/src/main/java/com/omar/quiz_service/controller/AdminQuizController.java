package com.omar.quiz_service.controller;

import com.omar.quiz_service.dto.*;
import com.omar.quiz_service.model.Quiz;
import com.omar.quiz_service.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/quiz")
public class AdminQuizController {

    @Autowired
    private QuizService quizService;

    @PostMapping()
    public ResponseEntity<ApiResponse<Quiz>> createQuiz (@RequestHeader("X-User-Id") Integer userId, @RequestBody CreateQuizRequest createQuizRequest) {
        return quizService.createQuiz(userId,createQuizRequest);
    }

    @PutMapping("/{quizId}")
    public ResponseEntity<ApiResponse<Quiz>> editQuiz (@RequestHeader("X-User-Id") Integer userId ,@PathVariable int quizId, @RequestBody CreateQuizRequest createQuizRequest) {
        return quizService.editQuiz(userId,quizId,createQuizRequest);
    }

    @DeleteMapping("/{quizId}")
    public ResponseEntity<ApiResponse<Quiz>> deleteQuiz (@RequestHeader("X-User-Id") int userId , @PathVariable int quizId) {
        return quizService.deleteQuiz(userId , quizId);
    }

    @DeleteMapping("/{quizId}/question/{questionId}")
    public ResponseEntity<ApiResponse<Quiz>> removeQuestionFromQuiz(@RequestHeader("X-User-Id") int userId , @PathVariable int quizId, @PathVariable int questionId) {
        return quizService.removeQuestionFromQuiz(userId , quizId , questionId);
    }

}
