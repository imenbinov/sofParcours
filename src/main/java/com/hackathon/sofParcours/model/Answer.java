package com.hackathon.sofParcours.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "answers")
public class Answer {
    @Id
    private String id;
    private String userId;
    private String questionId;
    private String quizId;
    private int selectedOptionIndex;
    private boolean isCorrect;
    private int pointsEarned;
    private long responseTimeMs; // Temps de réponse en millisecondes
    private LocalDateTime answeredAt;
}
