package com.hackathon.sofParcours.dto;

import java.util.List;
import java.util.Map;

/**
 * DTO pour le dashboard complet
 */
public class DashboardResponse {
    
    // Vue d'ensemble
    private OverviewStats overview;
    
    // Analytics par période
    private AnalyticsDTO todayStats;
    private AnalyticsDTO weekStats;
    private AnalyticsDTO monthStats;
    
    // Graphiques
    private List<ChartData> progressionChart; // XP au fil du temps
    private List<ChartData> accuracyChart; // Précision au fil du temps
    private List<ChartData> activityChart; // Activité quotidienne
    
    // Top performances
    private List<TopCategory> topCategories;
    private List<String> recentAchievements;
    
    // Recommandations
    private List<String> recommendations;
    
    // KPIs clés
    private Map<String, Object> keyMetrics;
    
    // Classes internes
    public static class OverviewStats {
        private int totalQuizCompleted;
        private int currentLevel;
        private int currentStreak;
        private double overallAccuracy;
        private int rank;
        private int totalUsers;

        // Getters et Setters
        public int getTotalQuizCompleted() {
            return totalQuizCompleted;
        }

        public void setTotalQuizCompleted(int totalQuizCompleted) {
            this.totalQuizCompleted = totalQuizCompleted;
        }

        public int getCurrentLevel() {
            return currentLevel;
        }

        public void setCurrentLevel(int currentLevel) {
            this.currentLevel = currentLevel;
        }

        public int getCurrentStreak() {
            return currentStreak;
        }

        public void setCurrentStreak(int currentStreak) {
            this.currentStreak = currentStreak;
        }

        public double getOverallAccuracy() {
            return overallAccuracy;
        }

        public void setOverallAccuracy(double overallAccuracy) {
            this.overallAccuracy = overallAccuracy;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public int getTotalUsers() {
            return totalUsers;
        }

        public void setTotalUsers(int totalUsers) {
            this.totalUsers = totalUsers;
        }
    }
    
    public static class ChartData {
        private String label;
        private Object value;
        private String date;

        public ChartData() {}
        
        public ChartData(String label, Object value, String date) {
            this.label = label;
            this.value = value;
            this.date = date;
        }

        // Getters et Setters
        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
    
    public static class TopCategory {
        private String name;
        private int quizCount;
        private double averageScore;

        public TopCategory() {}
        
        public TopCategory(String name, int quizCount, double averageScore) {
            this.name = name;
            this.quizCount = quizCount;
            this.averageScore = averageScore;
        }

        // Getters et Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getQuizCount() {
            return quizCount;
        }

        public void setQuizCount(int quizCount) {
            this.quizCount = quizCount;
        }

        public double getAverageScore() {
            return averageScore;
        }

        public void setAverageScore(double averageScore) {
            this.averageScore = averageScore;
        }
    }

    // Getters et Setters
    public OverviewStats getOverview() {
        return overview;
    }

    public void setOverview(OverviewStats overview) {
        this.overview = overview;
    }

    public AnalyticsDTO getTodayStats() {
        return todayStats;
    }

    public void setTodayStats(AnalyticsDTO todayStats) {
        this.todayStats = todayStats;
    }

    public AnalyticsDTO getWeekStats() {
        return weekStats;
    }

    public void setWeekStats(AnalyticsDTO weekStats) {
        this.weekStats = weekStats;
    }

    public AnalyticsDTO getMonthStats() {
        return monthStats;
    }

    public void setMonthStats(AnalyticsDTO monthStats) {
        this.monthStats = monthStats;
    }

    public List<ChartData> getProgressionChart() {
        return progressionChart;
    }

    public void setProgressionChart(List<ChartData> progressionChart) {
        this.progressionChart = progressionChart;
    }

    public List<ChartData> getAccuracyChart() {
        return accuracyChart;
    }

    public void setAccuracyChart(List<ChartData> accuracyChart) {
        this.accuracyChart = accuracyChart;
    }

    public List<ChartData> getActivityChart() {
        return activityChart;
    }

    public void setActivityChart(List<ChartData> activityChart) {
        this.activityChart = activityChart;
    }

    public List<TopCategory> getTopCategories() {
        return topCategories;
    }

    public void setTopCategories(List<TopCategory> topCategories) {
        this.topCategories = topCategories;
    }

    public List<String> getRecentAchievements() {
        return recentAchievements;
    }

    public void setRecentAchievements(List<String> recentAchievements) {
        this.recentAchievements = recentAchievements;
    }

    public List<String> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<String> recommendations) {
        this.recommendations = recommendations;
    }

    public Map<String, Object> getKeyMetrics() {
        return keyMetrics;
    }

    public void setKeyMetrics(Map<String, Object> keyMetrics) {
        this.keyMetrics = keyMetrics;
    }
}