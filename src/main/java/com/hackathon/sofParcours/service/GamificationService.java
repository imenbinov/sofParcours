package com.hackathon.sofParcours.service;

import com.hackathon.sofParcours.model.Achievement;
import com.hackathon.sofParcours.model.UserProgress;
import com.hackathon.sofParcours.repository.AchievementRepository;
import com.hackathon.sofParcours.repository.UserProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service de gamification : gestion XP, niveaux, achievements, streaks
 */
@Service
public class GamificationService {
    
    @Autowired
    private UserProgressRepository userProgressRepository;
    
    @Autowired
    private AchievementRepository achievementRepository;
    
    // Getter pour le repository (nécessaire pour ProgressionController)
    public UserProgressRepository getUserProgressRepository() {
        return userProgressRepository;
    }
    
    /**
     * Récupère ou crée la progression d'un utilisateur
     */
    public UserProgress getUserProgress(String userId) {
        return userProgressRepository.findByUserId(userId)
                .orElseGet(() -> {
                    UserProgress newProgress = new UserProgress(userId);
                    return userProgressRepository.save(newProgress);
                });
    }
    
    /**
     * Ajoute de l'XP à un utilisateur et vérifie les level ups
     */
    public UserProgress addXP(String userId, int xpAmount) {
        UserProgress progress = getUserProgress(userId);
        int oldLevel = progress.getLevel();
        
        progress.addXP(xpAmount);
        progress.updateStreak();
        
        UserProgress saved = userProgressRepository.save(progress);
        
        // Vérifier si level up pour débloquer achievements
        if (saved.getLevel() > oldLevel) {
            checkLevelAchievements(userId, saved.getLevel());
        }
        
        return saved;
    }
    
    /**
     * Enregistre la complétion d'un quiz
     */
    public List<Achievement> recordQuizCompletion(String userId, int score, boolean isPerfect) {
        UserProgress progress = getUserProgress(userId);
        
        // Mettre à jour les statistiques
        progress.setTotalQuizCompleted(progress.getTotalQuizCompleted() + 1);
        if (isPerfect) {
            progress.setPerfectScoresCount(progress.getPerfectScoresCount() + 1);
        }
        progress.updateStreak();
        
        // Ajouter XP basé sur le score
        int xpGained = calculateXPFromScore(score, isPerfect);
        progress.addXP(xpGained);
        
        userProgressRepository.save(progress);
        
        // Vérifier les achievements débloqués
        return checkAndUnlockAchievements(userId, progress);
    }
    
    /**
     * Enregistre une réponse à une question
     */
    public void recordAnswer(String userId, boolean isCorrect) {
        UserProgress progress = getUserProgress(userId);
        progress.setTotalQuestionsAnswered(progress.getTotalQuestionsAnswered() + 1);
        
        if (isCorrect) {
            progress.setCorrectAnswersCount(progress.getCorrectAnswersCount() + 1);
        }
        
        // Calculer le taux de précision
        double accuracy = (double) progress.getCorrectAnswersCount() / progress.getTotalQuestionsAnswered() * 100;
        progress.setAverageScore(accuracy);
        
        userProgressRepository.save(progress);
    }
    
    /**
     * Vérifie et débloque les achievements basés sur les statistiques
     */
    public List<Achievement> checkAndUnlockAchievements(String userId, UserProgress progress) {
        List<Achievement> newlyUnlocked = new ArrayList<>();
        List<Achievement> allAchievements = achievementRepository.findAll();
        
        for (Achievement achievement : allAchievements) {
            // Skip si déjà débloqué
            if (progress.getUnlockedAchievements().contains(achievement.getId())) {
                continue;
            }
            
            boolean shouldUnlock = false;
            
            // Vérifier selon la catégorie
            switch (achievement.getCategory()) {
                case "QUIZ_MASTER":
                    shouldUnlock = progress.getTotalQuizCompleted() >= achievement.getRequiredValue();
                    break;
                case "PERFECT_SCORE":
                    shouldUnlock = progress.getPerfectScoresCount() >= achievement.getRequiredValue();
                    break;
                case "STREAK_MASTER":
                    shouldUnlock = progress.getCurrentStreak() >= achievement.getRequiredValue();
                    break;
                case "LEVEL_MILESTONE":
                    shouldUnlock = progress.getLevel() >= achievement.getRequiredValue();
                    break;
                case "ACCURACY_EXPERT":
                    shouldUnlock = progress.getAverageScore() >= achievement.getRequiredValue();
                    break;
            }
            
            if (shouldUnlock) {
                progress.unlockAchievement(achievement.getId());
                progress.addXP(achievement.getXpReward());
                newlyUnlocked.add(achievement);
            }
        }
        
        if (!newlyUnlocked.isEmpty()) {
            userProgressRepository.save(progress);
        }
        
        return newlyUnlocked;
    }
    
    /**
     * Vérifie les achievements de niveau
     */
    private void checkLevelAchievements(String userId, int level) {
        List<Achievement> levelAchievements = achievementRepository.findByCategory("LEVEL_MILESTONE");
        UserProgress progress = getUserProgress(userId);
        
        for (Achievement achievement : levelAchievements) {
            if (level >= achievement.getRequiredValue() && 
                !progress.getUnlockedAchievements().contains(achievement.getId())) {
                progress.unlockAchievement(achievement.getId());
                progress.addXP(achievement.getXpReward());
            }
        }
        
        userProgressRepository.save(progress);
    }
    
    /**
     * Calcule l'XP gagné selon le score
     */
    private int calculateXPFromScore(int score, boolean isPerfect) {
        int baseXP = 50;
        int scoreBonus = (int) (score * 0.5); // 50 points = 25 XP
        int perfectBonus = isPerfect ? 50 : 0;
        
        return baseXP + scoreBonus + perfectBonus;
    }
    
    /**
     * Récupère tous les achievements
     */
    public List<Achievement> getAllAchievements() {
        return achievementRepository.findAll();
    }
    
    /**
     * Récupère les achievements d'un utilisateur
     */
    public List<Achievement> getUserAchievements(String userId) {
        UserProgress progress = getUserProgress(userId);
        List<String> unlockedIds = progress.getUnlockedAchievements();
        
        List<Achievement> achievements = new ArrayList<>();
        for (String id : unlockedIds) {
            achievementRepository.findById(id).ifPresent(achievements::add);
        }
        
        return achievements;
    }
    
    /**
     * Récupère le leaderboard par niveau
     */
    public List<UserProgress> getTopPlayers(int limit) {
        List<UserProgress> all = userProgressRepository.findTop10ByOrderByLevelDescCurrentXPDesc();
        return all.size() > limit ? all.subList(0, limit) : all;
    }
    
    /**
     * Initialise les achievements par défaut
     */
    public void initializeDefaultAchievements() {
        if (achievementRepository.count() > 0) {
            return; // Déjà initialisé
        }
        
        List<Achievement> defaults = List.of(
            // Quiz Master
            new Achievement("Premiers pas", "Complétez votre premier quiz", "🎯", 
                "QUIZ_MASTER", 1, 50, "COMMON", false),
            new Achievement("Habitué", "Complétez 10 quiz", "📚", 
                "QUIZ_MASTER", 10, 100, "COMMON", false),
            new Achievement("Expert", "Complétez 50 quiz", "🏆", 
                "QUIZ_MASTER", 50, 500, "RARE", false),
            new Achievement("Maître", "Complétez 100 quiz", "👑", 
                "QUIZ_MASTER", 100, 1000, "EPIC", false),
            
            // Perfect Score
            new Achievement("Sans faute", "Obtenez un score parfait", "⭐", 
                "PERFECT_SCORE", 1, 100, "COMMON", false),
            new Achievement("Perfectionniste", "Obtenez 5 scores parfaits", "🌟", 
                "PERFECT_SCORE", 5, 300, "RARE", false),
            new Achievement("Génie", "Obtenez 20 scores parfaits", "💎", 
                "PERFECT_SCORE", 20, 1000, "EPIC", false),
            
            // Streak
            new Achievement("En série", "Connectez-vous 3 jours de suite", "🔥", 
                "STREAK_MASTER", 3, 100, "COMMON", false),
            new Achievement("Assidu", "Connectez-vous 7 jours de suite", "🔥🔥", 
                "STREAK_MASTER", 7, 300, "RARE", false),
            new Achievement("Inébranlable", "Connectez-vous 30 jours de suite", "🔥🔥🔥", 
                "STREAK_MASTER", 30, 1500, "LEGENDARY", false),
            
            // Level Milestones
            new Achievement("Niveau 5", "Atteignez le niveau 5", "🎖️", 
                "LEVEL_MILESTONE", 5, 200, "COMMON", false),
            new Achievement("Niveau 10", "Atteignez le niveau 10", "🎖️🎖️", 
                "LEVEL_MILESTONE", 10, 500, "RARE", false),
            new Achievement("Niveau 25", "Atteignez le niveau 25", "🎖️🎖️🎖️", 
                "LEVEL_MILESTONE", 25, 1000, "EPIC", false),
            new Achievement("Niveau 50", "Atteignez le niveau 50", "👑", 
                "LEVEL_MILESTONE", 50, 5000, "LEGENDARY", false),
            
            // Secret Achievements
            new Achievement("Easter Egg", "Trouvez l'easter egg caché", "🥚", 
                "SECRET", 1, 500, "RARE", true),
            new Achievement("Nuit blanche", "Complétez un quiz après minuit", "🌙", 
                "SECRET", 1, 300, "RARE", true)
        );
        
        achievementRepository.saveAll(defaults);
    }
}