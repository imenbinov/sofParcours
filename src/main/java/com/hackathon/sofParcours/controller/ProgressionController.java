package com.hackathon.sofParcours.controller;

import com.hackathon.sofParcours.dto.ProgressionResponse;
import com.hackathon.sofParcours.model.Achievement;
import com.hackathon.sofParcours.model.UserProgress;
import com.hackathon.sofParcours.service.GamificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur pour la progression et la gamification
 */
@RestController
@RequestMapping("/api/progression")
@Tag(name = "Progression", description = "API de progression, niveaux, XP et achievements")
public class ProgressionController {
    
    @Autowired
    private GamificationService gamificationService;
    
    /**
     * Récupère la progression complète d'un utilisateur
     */
    @GetMapping("/{userId}")
    @Operation(summary = "Obtenir la progression d'un utilisateur", 
               description = "Récupère niveau, XP, achievements, streaks et statistiques")
    public ResponseEntity<ProgressionResponse> getUserProgression(@PathVariable String userId) {
        UserProgress progress = gamificationService.getUserProgress(userId);
        List<Achievement> achievements = gamificationService.getUserAchievements(userId);
        
        ProgressionResponse response = new ProgressionResponse();
        response.setUserId(progress.getUserId());
        response.setLevel(progress.getLevel());
        response.setTitle(progress.getTitle());
        response.setCurrentXP(progress.getCurrentXP());
        response.setXpToNextLevel(progress.getXpToNextLevel());
        
        // Calculer le pourcentage de progression
        double percentage = (double) progress.getCurrentXP() / progress.getXpToNextLevel() * 100;
        response.setProgressPercentage(Math.round(percentage * 100.0) / 100.0);
        
        // Statistiques
        response.setTotalQuizCompleted(progress.getTotalQuizCompleted());
        response.setTotalQuestionsAnswered(progress.getTotalQuestionsAnswered());
        response.setCorrectAnswersCount(progress.getCorrectAnswersCount());
        
        if (progress.getTotalQuestionsAnswered() > 0) {
            double accuracy = (double) progress.getCorrectAnswersCount() / 
                            progress.getTotalQuestionsAnswered() * 100;
            response.setAccuracyRate(Math.round(accuracy * 100.0) / 100.0);
        }
        
        response.setAverageScore(progress.getAverageScore());
        
        // Streaks
        response.setCurrentStreak(progress.getCurrentStreak());
        response.setLongestStreak(progress.getLongestStreak());
        
        // Achievements
        response.setTotalAchievementsUnlocked(achievements.size());
        response.setRecentAchievements(achievements.size() > 5 ? 
            achievements.subList(achievements.size() - 5, achievements.size()) : achievements);
        
        if (!achievements.isEmpty()) {
            response.setLastUnlocked(achievements.get(achievements.size() - 1));
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Ajoute de l'XP manuellement (pour tests)
     */
    @PostMapping("/{userId}/add-xp")
    @Operation(summary = "Ajouter de l'XP", 
               description = "Ajoute des points d'expérience à un utilisateur")
    public ResponseEntity<Map<String, Object>> addXP(
            @PathVariable String userId, 
            @RequestParam int amount) {
        
        int oldLevel = gamificationService.getUserProgress(userId).getLevel();
        UserProgress updated = gamificationService.addXP(userId, amount);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("xpAdded", amount);
        response.put("currentXP", updated.getCurrentXP());
        response.put("level", updated.getLevel());
        response.put("leveledUp", updated.getLevel() > oldLevel);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Récupère tous les achievements disponibles
     */
    @GetMapping("/achievements")
    @Operation(summary = "Liste des achievements", 
               description = "Récupère tous les achievements disponibles (hors secrets)")
    public ResponseEntity<List<Achievement>> getAllAchievements() {
        List<Achievement> all = gamificationService.getAllAchievements();
        // Filtrer les secrets non débloqués
        List<Achievement> visible = all.stream()
            .filter(a -> !a.isSecret())
            .toList();
        return ResponseEntity.ok(visible);
    }
    
    /**
     * Récupère les achievements d'un utilisateur
     */
    @GetMapping("/{userId}/achievements")
    @Operation(summary = "Achievements débloqués", 
               description = "Récupère tous les achievements débloqués par l'utilisateur")
    public ResponseEntity<List<Achievement>> getUserAchievements(@PathVariable String userId) {
        List<Achievement> achievements = gamificationService.getUserAchievements(userId);
        return ResponseEntity.ok(achievements);
    }
    
    /**
     * Leaderboard des meilleurs joueurs
     */
    @GetMapping("/leaderboard")
    @Operation(summary = "Leaderboard", 
               description = "Top 10 des joueurs par niveau et XP")
    public ResponseEntity<List<UserProgress>> getLeaderboard() {
        List<UserProgress> topPlayers = gamificationService.getTopPlayers(10);
        return ResponseEntity.ok(topPlayers);
    }
    
    /**
     * Initialise les achievements par défaut
     */
    @PostMapping("/init-achievements")
    @Operation(summary = "Initialiser achievements", 
               description = "Crée les achievements par défaut (admin)")
    public ResponseEntity<Map<String, String>> initializeAchievements() {
        gamificationService.initializeDefaultAchievements();
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Achievements initialisés avec succès");
        response.put("count", String.valueOf(gamificationService.getAllAchievements().size()));
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Enregistre une activité utilisateur (pour le streak)
     */
    @PostMapping("/{userId}/activity")
    @Operation(summary = "Enregistrer activité", 
               description = "Met à jour la streak de l'utilisateur")
    public ResponseEntity<Map<String, Object>> recordActivity(@PathVariable String userId) {
        UserProgress progress = gamificationService.getUserProgress(userId);
        int oldStreak = progress.getCurrentStreak();
        
        progress.updateStreak();
        gamificationService.getUserProgressRepository().save(progress);
        
        Map<String, Object> response = new HashMap<>();
        response.put("currentStreak", progress.getCurrentStreak());
        response.put("longestStreak", progress.getLongestStreak());
        response.put("streakIncreased", progress.getCurrentStreak() > oldStreak);
        
        return ResponseEntity.ok(response);
    }
}