package com.omar.quiz_service.service;


import com.omar.quiz_service.exception.InvalidDateException;
import com.omar.quiz_service.exception.InvalidTimeException;
import com.omar.quiz_service.exception.NotFoundException;
import com.omar.quiz_service.exception.UnAuthorizedException;
import com.omar.quiz_service.client.QuestionClient;
import com.omar.quiz_service.client.UserClient;
import com.omar.quiz_service.dto.*;
import com.omar.quiz_service.model.Quiz;
import com.omar.quiz_service.model.QuizAttempt;
import com.omar.quiz_service.repo.QuizAttemptRepo;
import com.omar.quiz_service.repo.QuizRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuizService {

    @Autowired
    private QuizRepo quizRepo;

    @Autowired
    private QuestionClient questionClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    private QuizAttemptRepo quizAttemptRepo;

    public ResponseEntity<ApiResponse<Quiz>> createQuiz(Integer userId, CreateQuizRequest createQuizRequest) {

        List<Integer> questions = questionClient.getQuestionsForQuiz(createQuizRequest.getCategory(), createQuizRequest.getNumQ()).getBody();
        Quiz quiz = new Quiz();
        quiz.setTitle(createQuizRequest.getTitle());
        quiz.setQuestions(questions);
        quiz.setCreatorUserId(userId);

        if (createQuizRequest.getDurationMinutes() <= 0) {
            throw new InvalidTimeException("Duration must be greater than 0");
        }

        // Time Logic:
        LocalDateTime startTime = createQuizRequest.getStartTime();
        LocalDateTime endTime = startTime.plusMinutes(createQuizRequest.getDurationMinutes());
        LocalDateTime now = LocalDateTime.now();

        if (startTime.isBefore(now)) {
            throw new InvalidDateException("Start time cannot be in the past");
        }

        quiz.setStartTime(startTime);
        quiz.setEndTime(endTime);
        quiz.setCreatedAt(LocalDateTime.now());

        quizRepo.save(quiz);

        ApiResponse<Quiz> response = new ApiResponse<>(
                quiz,
                "Quiz Created Successfully"
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<ApiResponse<List<QuizResponse>>> getQuiz(Integer userId, int id) {
        Quiz quiz = quizRepo.findById(id).orElseThrow(() -> new NotFoundException("Quiz Not Found"));

        if (!quiz.getCreatorUserId().equals(userId)) {

            QuizStatus status = quizStatus(quiz);

            if (status != QuizStatus.ACTIVE) {

                if (status == QuizStatus.SCHEDULED) {
                    throw new InvalidDateException(
                            "Quiz has not started yet"
                    );
                }

                throw new InvalidDateException(
                        "Quiz has already ended"
                );
            }
        }

        List<Integer> questionIds = quiz.getQuestions();

        List<QuizResponse> questions = questionClient.getQuestionsFromId(questionIds).getBody();

        ApiResponse<List<QuizResponse>> response = new ApiResponse<>(
                questions,
                "Quiz Questions Fetched Successfully"
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse<QuizAttemptResponse>> calculateResult(Integer userId, int id, List<QuizSubmitRes> quizSubmitRes) {

        Quiz quiz = quizRepo.findById(id).orElseThrow(() -> new NotFoundException("Quiz Not Found"));

        UserDto userDto = userClient.getUserById(userId);

        QuizStatus quizStatus = quizStatus(quiz);

        if (quizStatus != QuizStatus.ACTIVE) {

            if (quizStatus == QuizStatus.SCHEDULED) {
                throw new InvalidDateException(
                        "Quiz has not started yet"
                );
            }

            throw new InvalidDateException(
                    "Quiz has already ended"
            );
        }

        boolean alreadySubmitted =
                quizAttemptRepo.existsByQuizIdAndUserId(
                        quiz.getId(),
                        userId
                );

        if (alreadySubmitted) {
            throw new UnAuthorizedException("You already submitted this quiz");
        }

        Integer result = questionClient.getScore(quizSubmitRes).getBody();

        int total = quiz.getQuestions().size();
        int passMark = total / 2;

        QuizAttempt quizAttempt = new QuizAttempt();
        quizAttempt.setQuizId(quiz.getId());
        quizAttempt.setUserId(userId);
        quizAttempt.setScore(result);
        quizAttempt.setTotal(total);
        quizAttempt.setSubmittedAt(LocalDateTime.now());

        if (result >= passMark)
            quizAttempt.setResultStatus(ResultStatus.PASSED);
        else
            quizAttempt.setResultStatus(ResultStatus.FAILED);

        QuizAttempt savedAttempt = quizAttemptRepo.save(quizAttempt);

        QuizAttemptResponse resultResponse = new QuizAttemptResponse();
        resultResponse.setScore(savedAttempt.getScore());
        resultResponse.setTotal(savedAttempt.getTotal());
        resultResponse.setResultStatus(savedAttempt.getResultStatus());
        resultResponse.setQuizId(quiz.getId());
        resultResponse.setQuizTitle(quiz.getTitle());
        resultResponse.setUserId(userId);
        resultResponse.setUsername(userDto.getFirstName() + " " + userDto.getLastName());
        resultResponse.setSubmittedAt(savedAttempt.getSubmittedAt());


        ApiResponse<QuizAttemptResponse> response = new ApiResponse<>(
                resultResponse,
                "Your Result For " + quiz.getTitle()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public QuizStatus quizStatus(Quiz quiz) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(quiz.getStartTime())) {
            return QuizStatus.SCHEDULED;
        } else if (now.isAfter(quiz.getEndTime())) {
            return QuizStatus.FINISHED;
        } else {
            return QuizStatus.ACTIVE;
        }
    }

    public QuizAttemptResponse getUserAttempt(int userId, int quizId) {
        Quiz quiz = quizRepo.findById(quizId).orElseThrow(() -> new NotFoundException("Quiz Not Found"));

        UserDto userData = userClient.getUserById(userId);

        if(userData == null) {
            throw new NotFoundException("User Not Found");
        }

        QuizAttempt quizAttempt = quizAttemptRepo.findByQuizIdAndUserId(quizId, userId).orElseThrow(() -> new NotFoundException("No attempt found for this quiz"));

        QuizAttemptResponse quizAttemptResponse = new QuizAttemptResponse();
        quizAttemptResponse.setQuizTitle(quiz.getTitle());
        quizAttemptResponse.setQuizId(quiz.getId());
        quizAttemptResponse.setScore(quizAttempt.getScore());
        quizAttemptResponse.setTotal(quizAttempt.getTotal());
        quizAttemptResponse.setResultStatus(quizAttempt.getResultStatus());
        quizAttemptResponse.setSubmittedAt(quizAttempt.getSubmittedAt());
        quizAttemptResponse.setUserId(userId);
        quizAttemptResponse.setUsername(userData.getFirstName() + " "+userData.getLastName());

        return quizAttemptResponse;

    }

    public ResponseEntity<ApiResponse<Quiz>> editQuiz(Integer userId ,Integer quizId, CreateQuizRequest createQuizRequest) {
        Quiz quiz = quizRepo.findById(quizId)
                .orElseThrow(() -> new NotFoundException("Quiz not found"));


        if (!quiz.getCreatorUserId().equals(userId)) {
            throw new UnAuthorizedException("Not allowed to edit this quiz");
        }


        LocalDateTime startTime = createQuizRequest.getStartTime();
        if (startTime.isBefore(LocalDateTime.now())) {
            throw new InvalidDateException("Start time cannot be in the past");
        }

        if(createQuizRequest.getDurationMinutes() <= 0){
            throw new InvalidTimeException("Duration must be Greater than 0");
        }


        quiz.setTitle(createQuizRequest.getTitle());
        quiz.setStartTime(startTime);


        quiz.setEndTime(
                startTime.plusMinutes(createQuizRequest.getDurationMinutes())
        );


        List<Integer> newQuestions =
                questionClient.getQuestionsForQuiz(
                        createQuizRequest.getCategory(),
                        createQuizRequest.getNumQ()
                ).getBody();

        quiz.setQuestions(newQuestions);


        Quiz savedQuiz = quizRepo.save(quiz);


        ApiResponse<Quiz> response = new ApiResponse<>(
                savedQuiz,
                "Quiz Edited Successfully"
        );

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<Quiz>> deleteQuiz(int userId, int quizId) {
        Quiz quiz = quizRepo.findById(quizId)
                .orElseThrow(() -> new NotFoundException("Quiz not found"));

        // 🔐 Ownership check
        if (!quiz.getCreatorUserId().equals(userId)) {
            throw new UnAuthorizedException("Not allowed to delete this quiz");
        }

        quizRepo.delete(quiz);

        ApiResponse<Quiz> response = new ApiResponse<>(
                null,
                "Quiz Deleted Successfully"
        );
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<Quiz>> removeQuestionFromQuiz(int userId, int quizId, int questionId) {

        Quiz quiz = quizRepo.findById(quizId)
                .orElseThrow(() -> new NotFoundException("Quiz not found"));


        if (!quiz.getCreatorUserId().equals(userId)) {
            throw new UnAuthorizedException("Not allowed to edit this quiz");
        }


        boolean removed = quiz.getQuestions().remove(Integer.valueOf(questionId));

        if(!removed)
            throw new NotFoundException("Question Not Found in Quiz");

        quizRepo.save(quiz);

        ApiResponse<Quiz> response = new ApiResponse<>(
                null,
                "Question removed from quiz successfully"
        );

        return ResponseEntity.ok(response);

    }


    public ResponseEntity<List<QuizAttemptResponse>> getAllUserAttempt(Integer userId) {
        UserDto userDto = userClient.getUserById(userId);

        List<QuizAttempt> quizAttempts = quizAttemptRepo.findByUserId(userId);
        List<Integer> quizIds = quizAttempts.stream()
                .map(QuizAttempt::getQuizId)
                .toList();

        List<Quiz> quizzes = quizRepo.findAllById(quizIds);

        Map<Integer , Quiz> quizMap = quizzes.stream()
                .collect(Collectors.toMap(Quiz::getId, q -> q));


        List<QuizAttemptResponse> quizAttemptResponses = new ArrayList<>();
        for (QuizAttempt quizAttempt : quizAttempts) {
            QuizAttemptResponse quizAttemptResponse = new QuizAttemptResponse();
            Quiz quiz = quizMap.get(quizAttempt.getQuizId());
            quizAttemptResponse.setQuizId(quizAttempt.getQuizId());
            quizAttemptResponse.setQuizTitle(quiz.getTitle());
            quizAttemptResponse.setUserId(userId);
            quizAttemptResponse.setTotal(quizAttempt.getTotal());
            quizAttemptResponse.setScore(quizAttempt.getScore());
            quizAttemptResponse.setResultStatus(quizAttempt.getResultStatus());
            quizAttemptResponse.setSubmittedAt(quizAttempt.getSubmittedAt());
            quizAttemptResponse.setUsername(userDto.getFirstName() + " " + userDto.getLastName());

            quizAttemptResponses.add(quizAttemptResponse);
        }
        return ResponseEntity.ok(quizAttemptResponses);
    }

    public ResponseEntity<ApiResponse<List<QuizAttemptResponse>>> getAllQuizAttempts(int quizId) {

        List<QuizAttempt> quizAttempts = quizAttemptRepo.findByQuizId(quizId);

        if(quizAttempts.isEmpty()) {
           return ResponseEntity.ok((new ApiResponse<>(Collections.emptyList(),"No Attempts Found")));
        }

        List<Integer> userIds = quizAttempts.stream()
                .map(QuizAttempt::getUserId)
                .distinct()
                .toList();

        Map<Integer , UserDto> userDtoMap = userClient.getUsersByIds(userIds);

        List<QuizAttemptResponse> quizAttemptResponses = new ArrayList<>();

        for(QuizAttempt quizAttempt : quizAttempts) {
            UserDto userDto = userDtoMap.get(quizAttempt.getUserId());

            QuizAttemptResponse quizAttemptResponse = new QuizAttemptResponse();
            quizAttemptResponse.setQuizId(quizAttempt.getQuizId());
            quizAttemptResponse.setUserId(userDto.getId());
            quizAttemptResponse.setUsername(userDto.getFirstName() + " " + userDto.getLastName());
            quizAttemptResponse.setScore(quizAttempt.getScore());
            quizAttemptResponse.setTotal(quizAttempt.getTotal());
            quizAttemptResponse.setResultStatus(quizAttempt.getResultStatus());
            quizAttemptResponse.setSubmittedAt(quizAttempt.getSubmittedAt());
            quizAttemptResponse.setQuizTitle("Test For now ");

            quizAttemptResponses.add(quizAttemptResponse);
        }

        ApiResponse<List<QuizAttemptResponse>> response = new ApiResponse<>(
                quizAttemptResponses,
                "Users Result For Quiz Test For now"
        );

        return ResponseEntity.ok(response);
    }
}
