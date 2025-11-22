package com.hackathon.sofParcours.repository;

import com.hackathon.sofParcours.model.Quiz;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRepository extends MongoRepository<Quiz, String> {
    Optional<Quiz> findByRoomCode(String roomCode);
    List<Quiz> findAllByRoomCode(String roomCode);
}
