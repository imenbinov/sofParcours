package com.hackathon.sofParcours.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "quiz_history")
public class QuizHistory {
    @Id
    private String id;
    private String userId;
    private String quizId;
    private String roomCode;
    private int totalScore;
    private int correctAnswers;
    private int totalQuestions;
    private List<String> badgesEarned; // IDs des badges gagnés
    private int rank; // Classement dans cette room
    private LocalDateTime completedAt;
}
