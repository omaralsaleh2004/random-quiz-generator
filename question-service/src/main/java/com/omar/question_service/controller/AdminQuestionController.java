package com.omar.question_service.controller;
import com.omar.question_service.dto.ApiResponse;
import com.omar.question_service.model.Question;
import com.omar.question_service.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/question")
public class AdminQuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping()
    public ResponseEntity<ApiResponse<Question>> addQuestion(@RequestBody Question question) {
        return questionService.addQuestion(question);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Question>> deleteQuestion(@PathVariable int id) {
        return questionService.deleteQuestion(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Question>> updateQuestion(@RequestBody Question question, @PathVariable int id) {
        return questionService.updateQuestion(question,id);
    }

    @GetMapping("/allQuestions")
    public ResponseEntity<ApiResponse<List<Question>>> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<Question>>> getQuestionsByCategory(@PathVariable String category) {
        return questionService.getQuestionsByCategory(category);
    }


}
