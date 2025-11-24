package com.hackathon.sofParcours.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private List<String> badgesEarned = new ArrayList<>();
    private int rank;
    private LocalDateTime completedAt;
    private LocalDateTime startedAt;
    private LocalDateTime createdAt;

    public QuizHistory() {
        this.createdAt = LocalDateTime.now();
        this.completedAt = LocalDateTime.now();
    }

    public QuizHistory(String id, String userId, String quizId, String roomCode, int totalScore,
                      int correctAnswers, int totalQuestions, List<String> badgesEarned,
                      int rank, LocalDateTime completedAt, LocalDateTime startedAt) {
        this.id = id;
        this.userId = userId;
        this.quizId = quizId;
        this.roomCode = roomCode;
        this.totalScore = totalScore;
        this.correctAnswers = correctAnswers;
        this.totalQuestions = totalQuestions;
        this.badgesEarned = badgesEarned;
        this.rank = rank;
        this.completedAt = completedAt;
        this.startedAt = startedAt;
        this.createdAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getQuizId() { return quizId; }
    public void setQuizId(String quizId) { this.quizId = quizId; }

    public String getRoomCode() { return roomCode; }
    public void setRoomCode(String roomCode) { this.roomCode = roomCode; }

    public int getTotalScore() { return totalScore; }
    public void setTotalScore(int totalScore) { this.totalScore = totalScore; }

    public int getCorrectAnswers() { return correctAnswers; }
    public void setCorrectAnswers(int correctAnswers) { this.correctAnswers = correctAnswers; }

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

    public List<String> getBadgesEarned() { return badgesEarned; }
    public void setBadgesEarned(List<String> badgesEarned) { this.badgesEarned = badgesEarned; }

    public int getRank() { return rank; }
    public void setRank(int rank) { this.rank = rank; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Méthodes utilitaires pour calculer le score
    public double calculateScore() {
        if (totalQuestions == 0) return 0;
        return (double) correctAnswers / totalQuestions * 100;
    }
}
