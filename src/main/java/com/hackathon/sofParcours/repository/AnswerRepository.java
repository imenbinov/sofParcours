package com.hackathon.sofParcours.repository;

import com.hackathon.sofParcours.model.Answer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends MongoRepository<Answer, String> {
    List<Answer> findByQuizIdAndUserId(String quizId, String userId);
    List<Answer> findByQuizId(String quizId);
}
