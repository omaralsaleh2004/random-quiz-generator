package com.omar.quiz_service.repo;

import com.omar.quiz_service.model.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizAttemptRepo extends JpaRepository<QuizAttempt , Integer> {
    Boolean existsByQuizIdAndUserId(Integer id, Integer userId);

    Optional<QuizAttempt> findByQuizIdAndUserId(int quizId, int userId);

    List<QuizAttempt> findByUserId(Integer userId);
}
