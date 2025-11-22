package com.hackathon.sofParcours.repository;

import com.hackathon.sofParcours.model.QuizHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizHistoryRepository extends MongoRepository<QuizHistory, String> {
    List<QuizHistory> findByUserId(String userId);
    List<QuizHistory> findByRoomCode(String roomCode);
}
