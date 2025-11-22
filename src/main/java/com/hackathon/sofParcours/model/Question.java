package com.hackathon.sofParcours.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "questions")
public class Question {
    @Id
    private String id;
    private String text;
    private List<String> options; // Liste des options de réponse
    private int correctOptionIndex; // Index de la bonne réponse (0-based)
    private int points; // Points attribués pour cette question
    private int timeLimit; // Temps limite en secondes
    private String difficulty; // EASY, MEDIUM, HARD
    private String category; // ex: "Java", "Spring", "Architecture"
}
