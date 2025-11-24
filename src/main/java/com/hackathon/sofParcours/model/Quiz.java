package com.hackathon.sofParcours.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "quizzes")
public class Quiz {
    @Id
    private String id;
    private String title;
    private String description;
    private String roomCode;
    private List<String> questionIds = new ArrayList<>();
    private String status; // PENDING, IN_PROGRESS, COMPLETED
    private int currentQuestionIndex;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private String topic;
    private String difficulty; // EASY, MEDIUM, HARD
    private String category;

    /**
     * Liste des questions associées à ce quiz
     */
    @Transient
    private List<Question> questions;

    public Quiz() {}

    public Quiz(String id, String title, String description, String roomCode, List<String> questionIds, 
               String status, int currentQuestionIndex, String createdBy, LocalDateTime createdAt, 
               LocalDateTime startedAt, LocalDateTime completedAt, String topic, String difficulty, String category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.roomCode = roomCode;
        this.questionIds = questionIds;
        this.status = status;
        this.currentQuestionIndex = currentQuestionIndex;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
        this.topic = topic;
        this.difficulty = difficulty;
        this.category = category;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getRoomCode() { return roomCode; }
    public void setRoomCode(String roomCode) { this.roomCode = roomCode; }

    public List<String> getQuestionIds() { return questionIds; }
    public void setQuestionIds(List<String> questionIds) { this.questionIds = questionIds; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getCurrentQuestionIndex() { return currentQuestionIndex; }
    public void setCurrentQuestionIndex(int currentQuestionIndex) { this.currentQuestionIndex = currentQuestionIndex; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void setRoomId(String roomId) {
        this.roomCode = roomId;
    }

    public String getRoomId() {
        return this.roomCode;
    }

    public void setDuration(int duration) {
        // Ajouter un champ duration si nécessaire
    }

    public Integer getDuration() {
        return 30; // Valeur par défaut
    }
}
