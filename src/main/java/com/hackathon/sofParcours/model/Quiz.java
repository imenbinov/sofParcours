package com.hackathon.sofParcours.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "quizzes")
public class Quiz {
    @Id
    private String id;
    private String title;
    private String description;
    private String roomCode; // Room associée
    private List<String> questionIds; // IDs des questions
    private String status; // PENDING, IN_PROGRESS, FINISHED
    private int currentQuestionIndex; // Index de la question actuelle
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private String createdBy; // User ID de l'organisateur
}
