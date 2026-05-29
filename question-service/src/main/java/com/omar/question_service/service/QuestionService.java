package com.omar.question_service.service;


import com.omar.question_service.Exception.InvalidDataException;
import com.omar.question_service.Exception.NotFoundException;
import com.omar.question_service.dto.ApiResponse;
import com.omar.question_service.dto.QuizResponse;
import com.omar.question_service.dto.QuizSubmitRes;
import com.omar.question_service.model.Question;
import com.omar.question_service.repo.QuestionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Set;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepo questionRepo;

    public ResponseEntity<ApiResponse<List<Question>>> getAllQuestions() {

        List<Question> questions = questionRepo.findAll();

        ApiResponse<List<Question>> response = new ApiResponse<>(
                questions,
                "data fetched Successfully"
        );

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<List<Question>>> getQuestionsByCategory(String category) {
        List<Question> questions = questionRepo.findByCategory(category);

        ApiResponse<List<Question>> response = new ApiResponse<>(
                questions,
                "data fetched Successfully"
        );

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<Question>> addQuestion(Question question) {

        if (question.getQuestionTitle() == null ||
                question.getQuestionTitle().trim().isEmpty()) {

            throw new InvalidDataException("Question title is required");
        }

        if (question.getOption1() == null ||
                question.getOption1().trim().isEmpty()) {

            throw new InvalidDataException("Option 1 is required");
        }

        if (question.getOption2() == null ||
                question.getOption2().trim().isEmpty()) {

            throw new InvalidDataException("Option 2 is required");
        }

        if (question.getOption3() == null ||
                question.getOption3().trim().isEmpty()) {

            throw new InvalidDataException("Option 3 is required");
        }

        if (question.getOption4() == null ||
                question.getOption4().trim().isEmpty()) {

            throw new InvalidDataException("Option 4 is required");
        }

        if (question.getCategory() == null ||
                question.getOption4().trim().isEmpty()) {

            throw new InvalidDataException("Category is Required");
        }

        if (question.getDifficultyLevel() == null ||
                question.getOption4().trim().isEmpty()) {

            throw new InvalidDataException("Difficulty Level is Required");
        }

        if (question.getRightAnswer() == null ||
                question.getOption4().trim().isEmpty()) {

            throw new InvalidDataException("Right Answer is Required");
        }
        Question savedQuestion = questionRepo.save(question);

        ApiResponse<Question> response = new ApiResponse<>(
                savedQuestion,
                "Data Added Successfully"
        );

        return new ResponseEntity<>(response , HttpStatus.CREATED);
    }

    public ResponseEntity<ApiResponse<Question>> deleteQuestion(int id) {
        Question question = questionRepo.findById(id).orElseThrow(() -> new NotFoundException("Question Not Found"));
        questionRepo.deleteById(id);

        ApiResponse<Question> response = new ApiResponse<>(
                question,
                "Question Deleted Successfully"
        );

        return ResponseEntity.ok(response);
    }


    public ResponseEntity<ApiResponse<Question>> updateQuestion(Question q, int id) {

        Question existing = questionRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Question Not Found"));

        if (q.getQuestionTitle() != null)
            existing.setQuestionTitle(q.getQuestionTitle());

        if (q.getOption1() != null)
            existing.setOption1(q.getOption1());

        if (q.getOption2() != null)
            existing.setOption2(q.getOption2());

        if (q.getOption3() != null)
            existing.setOption3(q.getOption3());

        if (q.getOption4() != null)
            existing.setOption4(q.getOption4());

        if (q.getRightAnswer() != null)
            existing.setRightAnswer(q.getRightAnswer());

        if(q.getCategory() !=null)
            existing.setCategory(q.getCategory());

        if(q.getDifficultyLevel() != null)
            existing.setDifficultyLevel(q.getDifficultyLevel());

        questionRepo.save(existing);

        ApiResponse<Question> response = new ApiResponse<>(
              existing,
              "Question Updated Successfully"
        );

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<List<Integer>> getQuestionsForQuiz(String category , Integer numQ) {
        List<Integer> questions = questionRepo.findByRandomQuestionsByCategory(category , numQ);
        return new ResponseEntity<>(questions , HttpStatus.OK);
    }

    public ResponseEntity<List<QuizResponse>> getQuestionsFromId(List<Integer> questionIds) {
        List<Question> questions = questionRepo.findAllById(questionIds);
        List<QuizResponse> quizResponses = new ArrayList<>();

        for (Question q : questions) {
            QuizResponse quizResponse = new QuizResponse(q.getId(),q.getQuestionTitle() , q.getOption1(),q.getOption2(),q.getOption3(),q.getOption4());
            quizResponses.add(quizResponse);
        }

        return new ResponseEntity<>(quizResponses , HttpStatus.OK);
    }

    public ResponseEntity<Integer> calculateScore(List<QuizSubmitRes> quizSubmitRes) {
        int correct = 0;

        for(QuizSubmitRes q : quizSubmitRes) {
            Question question = questionRepo.findById(q.id()).orElseThrow(() -> new NotFoundException("Quiz Not Found"));
            if(q.response().equals(question.getRightAnswer()))
                correct++;
        }
        return new ResponseEntity<>(correct , HttpStatus.OK);
    }
}
