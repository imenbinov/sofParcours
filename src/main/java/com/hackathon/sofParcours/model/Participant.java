package com.hackathon.sofParcours.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "participants")
public class Participant {
    @Id
    private String id;
    private String userId;
    private String roomCode;
    private int currentScore; // Score temporaire dans cette room
    private boolean isOrganizer;
}
