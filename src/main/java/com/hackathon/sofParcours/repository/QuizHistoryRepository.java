package com.hackathon.sofParcours.repository;

import com.hackathon.sofParcours.model.QuizHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuizHistoryRepository extends MongoRepository<QuizHistory, String> {
    List<QuizHistory> findByUserId(String userId);
    List<QuizHistory> findByQuizId(String quizId);
    List<QuizHistory> findByRoomCode(String roomCode);
    List<QuizHistory> findByUserIdAndCreatedAtBetween(String userId, LocalDateTime start, LocalDateTime end);
}
