package com.omar.user_service.service;

import com.omar.user_service.client.QuizClient;
import com.omar.user_service.dto.QuizAttemptResponse;
import com.omar.user_service.dto.UserDto;
import com.omar.user_service.exception.NotFoundException;
import com.omar.user_service.model.User;
import com.omar.user_service.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private QuizClient quizClient;

    public UserDto getUserById(int userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new NotFoundException("User not Found"));

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());

        return userDto;
    }

    public ResponseEntity<QuizAttemptResponse> getAttempt(int userId, int quizId) {
            QuizAttemptResponse quizAttemptResponse = quizClient.getUserAttempt(userId, quizId);
            return ResponseEntity.ok(quizAttemptResponse);
    }
}
