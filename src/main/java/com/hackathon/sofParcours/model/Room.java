package com.hackathon.sofParcours.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "rooms")
public class Room {
    @Id
    private String id;
    
    private String name;
    private String description;
    private String code;
    
    /**
     * Slug normalisé pour recherche idempotente
     * Ex: "DevOps avancé" → "devops-avance"
     */
    @Indexed(unique = true)
    private String slug;
    
    /**
     * Indique si la Room a été générée par IA
     */
    private Boolean generatedByAI = false;
    
    /**
     * Date de génération par IA
     */
    private LocalDateTime generatedAt;
    
    /**
     * Quiz associé à cette Room
     */
    @Transient
    private Quiz quiz;
    
    private String createdBy;
    private List<String> participantIds = new ArrayList<>();
    private String currentQuizId;
    private String status; // WAITING, ACTIVE, CLOSED
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Room() {}

    public Room(String id, String code, String name, String description, String createdBy, 
               List<String> participantIds, String currentQuizId, String status, 
               LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
        this.participantIds = participantIds;
        this.currentQuizId = currentQuizId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public List<String> getParticipantIds() { return participantIds; }
    public void setParticipantIds(List<String> participantIds) { this.participantIds = participantIds; }

    public String getCurrentQuizId() { return currentQuizId; }
    public void setCurrentQuizId(String currentQuizId) { this.currentQuizId = currentQuizId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Getters et setters pour les nouveaux champs

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Boolean getGeneratedByAI() {
        return generatedByAI;
    }

    public void setGeneratedByAI(Boolean generatedByAI) {
        this.generatedByAI = generatedByAI;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public void setParticipants(List<String> participants) {
        this.participantIds = participants;
    }
}
