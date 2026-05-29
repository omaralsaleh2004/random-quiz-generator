package com.omar.question_service.controller;

import com.omar.question_service.dto.ApiResponse;
import com.omar.question_service.dto.QuizResponse;
import com.omar.question_service.dto.QuizSubmitRes;
import com.omar.question_service.model.Question;
import com.omar.question_service.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/generate")
    public ResponseEntity<List<Integer>> getQuestionsForQuiz(@RequestParam String category , @RequestParam Integer numQ) {
        return questionService.getQuestionsForQuiz(category , numQ);
    }

    @PostMapping("/getQuestions")
    public ResponseEntity<List<QuizResponse>> getQuestionsFromId (@RequestBody List<Integer> questionIds) {
        return questionService.getQuestionsFromId(questionIds);
    }

    @PostMapping("/getScore")
    public ResponseEntity<Integer> getScore(@RequestBody List<QuizSubmitRes> quizSubmitRes) {
        return questionService.calculateScore(quizSubmitRes);
    }

}
