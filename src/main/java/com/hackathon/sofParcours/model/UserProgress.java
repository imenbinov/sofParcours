package com.hackathon.sofParcours.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Modèle pour suivre la progression d'un utilisateur
 */
@Document(collection = "user_progress")
public class UserProgress {
    
    @Id
    private String id;
    private String userId;
    
    // Système de niveaux et XP
    private int level;
    private int currentXP;
    private int xpToNextLevel;
    private String title; // "Novice", "Apprentice", "Expert", "Master", "Legend"
    
    // Statistiques
    private int totalQuizCompleted;
    private int totalQuestionsAnswered;
    private int correctAnswersCount;
    private int perfectScoresCount; // Quiz avec 100%
    private double averageScore;
    
    // Achievements
    private List<String> unlockedAchievements; // IDs des achievements débloqués
    private LocalDateTime lastAchievementUnlocked;
    
    // Streaks (séries de jours consécutifs)
    private int currentStreak;
    private int longestStreak;
    private LocalDateTime lastActivityDate;
    
    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructeurs
    public UserProgress() {
        this.level = 1;
        this.currentXP = 0;
        this.xpToNextLevel = 100;
        this.title = "Novice";
        this.unlockedAchievements = new ArrayList<>();
        this.currentStreak = 0;
        this.longestStreak = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public UserProgress(String userId) {
        this();
        this.userId = userId;
    }
    
    // Méthodes utilitaires
    public void addXP(int xp) {
        this.currentXP += xp;
        checkLevelUp();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void unlockAchievement(String achievementId) {
        if (!this.unlockedAchievements.contains(achievementId)) {
            this.unlockedAchievements.add(achievementId);
            this.lastAchievementUnlocked = LocalDateTime.now();
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    private void checkLevelUp() {
        while (this.currentXP >= this.xpToNextLevel) {
            this.currentXP -= this.xpToNextLevel;
            this.level++;
            this.xpToNextLevel = calculateXPForNextLevel();
            updateTitle();
        }
    }
    
    private int calculateXPForNextLevel() {
        // Formule: XP nécessaire = 100 * level * 1.5
        return (int) (100 * this.level * 1.5);
    }
    
    private void updateTitle() {
        if (level >= 50) this.title = "Legend";
        else if (level >= 30) this.title = "Master";
        else if (level >= 15) this.title = "Expert";
        else if (level >= 5) this.title = "Apprentice";
        else this.title = "Novice";
    }
    
    public void updateStreak() {
        LocalDateTime today = LocalDateTime.now().toLocalDate().atStartOfDay();
        if (lastActivityDate == null) {
            currentStreak = 1;
        } else {
            LocalDateTime yesterday = today.minusDays(1);
            LocalDateTime lastActivityDay = lastActivityDate.toLocalDate().atStartOfDay();
            
            if (lastActivityDay.equals(yesterday)) {
                currentStreak++;
            } else if (!lastActivityDay.equals(today)) {
                currentStreak = 1;
            }
        }
        
        if (currentStreak > longestStreak) {
            longestStreak = currentStreak;
        }
        lastActivityDate = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getPerfectScoresCount() {
        return perfectScoresCount;
    }

    public void setPerfectScoresCount(int perfectScoresCount) {
        this.perfectScoresCount = perfectScoresCount;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    public List<String> getUnlockedAchievements() {
        return unlockedAchievements;
    }

    public void setUnlockedAchievements(List<String> unlockedAchievements) {
        this.unlockedAchievements = unlockedAchievements;
    }

    public LocalDateTime getLastAchievementUnlocked() {
        return lastAchievementUnlocked;
    }

    public void setLastAchievementUnlocked(LocalDateTime lastAchievementUnlocked) {
        this.lastAchievementUnlocked = lastAchievementUnlocked;
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

    public LocalDateTime getLastActivityDate() {
        return lastActivityDate;
    }

    public void setLastActivityDate(LocalDateTime lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
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