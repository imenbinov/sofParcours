package com.hackathon.sofParcours.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "rooms")
public class Room {
    @Id
    private String id;
    private String code; // Code unique à 6 chiffres
    private String name;
    private String organizerId; // ID de l'utilisateur organisateur
    private List<String> participantIds = new ArrayList<>(); // Liste des IDs participants
    private String status; // WAITING, IN_PROGRESS, FINISHED
    private String currentQuizId; // Quiz en cours (si applicable)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
