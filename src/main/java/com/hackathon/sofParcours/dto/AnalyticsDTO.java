package com.hackathon.sofParcours.dto;

import java.util.Map;

/**
 * DTO pour les métriques analytics
 */
public class AnalyticsDTO {
    
    private String period; // daily, weekly, monthly
    private String startDate;
    private String endDate;
    
    // Engagement
    private int totalSessions;
    private int averageSessionDuration;
    private int totalTimeSpent;
    private int activeDays;
    
    // Performance
    private int quizCompleted;
    private int questionsAnswered;
    private double accuracyRate;
    private double averageScore;
    private int perfectScores;
    
    // Progression
    private int xpGained;
    private int levelsGained;
    private int achievementsUnlocked;
    
    // Par catégorie
    private Map<String, Integer> quizzesByCategory;
    private Map<String, Double> scoresByCategory;
    
    // Comparaison avec période précédente
    private Double growthRate;
    private String trend; // "up", "down", "stable"

    // Constructeurs
    public AnalyticsDTO() {}

    // Getters et Setters
    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getTotalSessions() {
        return totalSessions;
    }

    public void setTotalSessions(int totalSessions) {
        this.totalSessions = totalSessions;
    }

    public int getAverageSessionDuration() {
        return averageSessionDuration;
    }

    public void setAverageSessionDuration(int averageSessionDuration) {
        this.averageSessionDuration = averageSessionDuration;
    }

    public int getTotalTimeSpent() {
        return totalTimeSpent;
    }

    public void setTotalTimeSpent(int totalTimeSpent) {
        this.totalTimeSpent = totalTimeSpent;
    }

    public int getActiveDays() {
        return activeDays;
    }

    public void setActiveDays(int activeDays) {
        this.activeDays = activeDays;
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

    public Double getGrowthRate() {
        return growthRate;
    }

    public void setGrowthRate(Double growthRate) {
        this.growthRate = growthRate;
    }

    public String getTrend() {
        return trend;
    }

    public void setTrend(String trend) {
        this.trend = trend;
    }
}