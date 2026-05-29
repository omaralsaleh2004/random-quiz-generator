package com.omar.quiz_service.client;


import com.omar.quiz_service.dto.QuizResponse;
import com.omar.quiz_service.dto.QuizSubmitRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(name = "question-service")
public interface QuestionClient {

    @GetMapping("/question/generate")
    public ResponseEntity<List<Integer>> getQuestionsForQuiz(@RequestParam String category , @RequestParam Integer numQ);

    @PostMapping("/question/getQuestions")
    public ResponseEntity<List<QuizResponse>> getQuestionsFromId (@RequestBody List<Integer> questionIds);

    @PostMapping("/question/getScore")
    public ResponseEntity<Integer> getScore(@RequestBody List<QuizSubmitRes> quizSubmitRes);
}
