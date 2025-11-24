package com.hackathon.sofParcours.dto;

import com.hackathon.sofParcours.model.Achievement;
import java.util.List;

/**
 * DTO pour les réponses de progression
 */
public class ProgressionResponse {
    
    private String userId;
    private int level;
    private String title;
    private int currentXP;
    private int xpToNextLevel;
    private double progressPercentage;
    
    // Statistiques
    private int totalQuizCompleted;
    private int totalQuestionsAnswered;
    private int correctAnswersCount;
    private double accuracyRate;
    private double averageScore;
    
    // Streaks
    private int currentStreak;
    private int longestStreak;
    
    // Achievements
    private int totalAchievementsUnlocked;
    private List<Achievement> recentAchievements;
    private Achievement lastUnlocked;
    
    // Leaderboard position
    private Integer globalRank;
    
    // Constructeurs
    public ProgressionResponse() {}

    // Getters et Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCurrentXP() {
        return currentXP;
    }

    public void setCurrentXP(int currentXP) {
        this.currentXP = currentXP;
    }

    public int getXpToNextLevel() {
        return xpToNextLevel;
    }

    public void setXpToNextLevel(int xpToNextLevel) {
        this.xpToNextLevel = xpToNextLevel;
    }

    public double getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(double progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public int getTotalQuizCompleted() {
        return totalQuizCompleted;
    }

    public void setTotalQuizCompleted(int totalQuizCompleted) {
        this.totalQuizCompleted = totalQuizCompleted;
    }

    public int getTotalQuestionsAnswered() {
        return totalQuestionsAnswered;
    }

    public void setTotalQuestionsAnswered(int totalQuestionsAnswered) {
        this.totalQuestionsAnswered = totalQuestionsAnswered;
    }

    public int getCorrectAnswersCount() {
        return correctAnswersCount;
    }

    public void setCorrectAnswersCount(int correctAnswersCount) {
        this.correctAnswersCount = correctAnswersCount;
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

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public int getLongestStreak() {
        return longestStreak;
    }

    public void setLongestStreak(int longestStreak) {
        this.longestStreak = longestStreak;
    }

    public int getTotalAchievementsUnlocked() {
        return totalAchievementsUnlocked;
    }

    public void setTotalAchievementsUnlocked(int totalAchievementsUnlocked) {
        this.totalAchievementsUnlocked = totalAchievementsUnlocked;
    }

    public List<Achievement> getRecentAchievements() {
        return recentAchievements;
    }

    public void setRecentAchievements(List<Achievement> recentAchievements) {
        this.recentAchievements = recentAchievements;
    }

    public Achievement getLastUnlocked() {
        return lastUnlocked;
    }

    public void setLastUnlocked(Achievement lastUnlocked) {
        this.lastUnlocked = lastUnlocked;
    }

    public Integer getGlobalRank() {
        return globalRank;
    }

    public void setGlobalRank(Integer globalRank) {
        this.globalRank = globalRank;
    }
}