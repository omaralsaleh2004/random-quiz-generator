package com.omar.question_service.repo;


import com.omar.question_service.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepo extends JpaRepository<Question, Integer> {

    @Query("Select q from Question q Where Lower(q.category) =Lower (:category)")
    List<Question> findByCategory(@Param("category") String category);

    @Query(value = """
        SELECT id FROM question
        WHERE LOWER(category) = LOWER(:category)
        ORDER BY RANDOM()
        Limit :numQ
        """, nativeQuery = true)
    List<Integer> findByRandomQuestionsByCategory(
            @Param("category") String category,
            @Param("numQ") int numQ
    );
}
