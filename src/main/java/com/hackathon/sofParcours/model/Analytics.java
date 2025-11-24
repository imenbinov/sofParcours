package com.hackathon.sofParcours.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Modèle pour stocker les analytics et métriques
 */
@Document(collection = "analytics")
public class Analytics {
    
    @Id
    private String id;
    private String userId;
    private String type; // DAILY, WEEKLY, MONTHLY, GLOBAL
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    
    // Métriques d'engagement
    private int sessionsCount;
    private int totalTimeSpentMinutes;
    private int averageSessionDurationMinutes;
    private int daysActive;
    
    // Métriques de performance
    private int quizCompleted;
    private int questionsAnswered;
    private int correctAnswers;
    private double accuracyRate;
    private double averageScore;
    private int perfectScores;
    
    // Progression
    private int xpGained;
    private int levelsGained;
    private int achievementsUnlocked;
    private int streakDays;
    
    // Métriques sociales
    private int commentsPosted;
    private int feedbackGiven;
    private int roomsJoined;
    
    // Métriques par catégorie (map: category -> count)
    private Map<String, Integer> quizzesByCategory;
    private Map<String, Double> scoresByCategory;
    
    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructeurs
    public Analytics() {
        this.quizzesByCategory = new HashMap<>();
        this.scoresByCategory = new HashMap<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Analytics(String userId, String type) {
        this();
        this.userId = userId;
        this.type = type;
    }

    // Getters et Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(LocalDateTime periodStart) {
        this.periodStart = periodStart;
    }

    public LocalDateTime getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(LocalDateTime periodEnd) {
        this.periodEnd = periodEnd;
    }

    public int getSessionsCount() {
        return sessionsCount;
    }

    public void setSessionsCount(int sessionsCount) {
        this.sessionsCount = sessionsCount;
    }

    public int getTotalTimeSpentMinutes() {
        return totalTimeSpentMinutes;
    }

    public void setTotalTimeSpentMinutes(int totalTimeSpentMinutes) {
        this.totalTimeSpentMinutes = totalTimeSpentMinutes;
    }

    public int getAverageSessionDurationMinutes() {
        return averageSessionDurationMinutes;
    }

    public void setAverageSessionDurationMinutes(int averageSessionDurationMinutes) {
        this.averageSessionDurationMinutes = averageSessionDurationMinutes;
    }

    public int getDaysActive() {
        return daysActive;
    }

    public void setDaysActive(int daysActive) {
        this.daysActive = daysActive;
    }

    public int getQuizCompleted() {
        return quizCompleted;
    }

    public void setQuizCompleted(int quizCompleted) {
        this.quizCompleted = quizCompleted;
    }

    public int getQuestionsAnswered() {
        return questionsAnswered;
    }

    public void setQuestionsAnswered(int questionsAnswered) {
        this.questionsAnswered = questionsAnswered;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public double getAccuracyRate() {
        return accuracyRate;
    }

    public void setAccuracyRate(double accuracyRate) {
        this.accuracyRate = accuracyRate;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    public int getPerfectScores() {
        return perfectScores;
    }

    public void setPerfectScores(int perfectScores) {
        this.perfectScores = perfectScores;
    }

    public int getXpGained() {
        return xpGained;
    }

    public void setXpGained(int xpGained) {
        this.xpGained = xpGained;
    }

    public int getLevelsGained() {
        return levelsGained;
    }

    public void setLevelsGained(int levelsGained) {
        this.levelsGained = levelsGained;
    }

    public int getAchievementsUnlocked() {
        return achievementsUnlocked;
    }

    public void setAchievementsUnlocked(int achievementsUnlocked) {
        this.achievementsUnlocked = achievementsUnlocked;
    }

    public int getStreakDays() {
        return streakDays;
    }

    public void setStreakDays(int streakDays) {
        this.streakDays = streakDays;
    }

    public int getCommentsPosted() {
        return commentsPosted;
    }

    public void setCommentsPosted(int commentsPosted) {
        this.commentsPosted = commentsPosted;
    }

    public int getFeedbackGiven() {
        return feedbackGiven;
    }

    public void setFeedbackGiven(int feedbackGiven) {
        this.feedbackGiven = feedbackGiven;
    }

    public int getRoomsJoined() {
        return roomsJoined;
    }

    public void setRoomsJoined(int roomsJoined) {
        this.roomsJoined = roomsJoined;
    }

    public Map<String, Integer> getQuizzesByCategory() {
        return quizzesByCategory;
    }

    public void setQuizzesByCategory(Map<String, Integer> quizzesByCategory) {
        this.quizzesByCategory = quizzesByCategory;
    }

    public Map<String, Double> getScoresByCategory() {
        return scoresByCategory;
    }

    public void setScoresByCategory(Map<String, Double> scoresByCategory) {
        this.scoresByCategory = scoresByCategory;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}