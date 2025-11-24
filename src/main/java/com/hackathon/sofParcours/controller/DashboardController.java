package com.hackathon.sofParcours.controller;

import com.hackathon.sofParcours.dto.AnalyticsDTO;
import com.hackathon.sofParcours.dto.DashboardResponse;
import com.hackathon.sofParcours.model.UserProgress;
import com.hackathon.sofParcours.model.Achievement;
import com.hackathon.sofParcours.service.AnalyticsService;
import com.hackathon.sofParcours.service.GamificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Contrôleur pour le dashboard et analytics
 */
@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "API pour tableaux de bord et analytics")
public class DashboardController {
    
    @Autowired
    private AnalyticsService analyticsService;
    
    @Autowired
    private GamificationService gamificationService;
    
    /**
     * Dashboard complet pour un utilisateur
     */
    @GetMapping("/{userId}")
    @Operation(summary = "Dashboard utilisateur", 
               description = "Récupère le dashboard complet avec toutes les métriques")
    public ResponseEntity<DashboardResponse> getUserDashboard(@PathVariable String userId) {
        DashboardResponse dashboard = new DashboardResponse();
        
        // Overview
        UserProgress progress = gamificationService.getUserProgress(userId);
        DashboardResponse.OverviewStats overview = new DashboardResponse.OverviewStats();
        overview.setTotalQuizCompleted(progress.getTotalQuizCompleted());
        overview.setCurrentLevel(progress.getLevel());
        overview.setCurrentStreak(progress.getCurrentStreak());
        overview.setOverallAccuracy(progress.getAverageScore());
        
        // Calculer le rang
        List<UserProgress> allUsers = gamificationService.getTopPlayers(1000);
        int rank = 1;
        for (UserProgress p : allUsers) {
            if (p.getUserId().equals(userId)) {
                overview.setRank(rank);
                break;
            }
            rank++;
        }
        overview.setTotalUsers(allUsers.size());
        dashboard.setOverview(overview);
        
        // Analytics par période
        dashboard.setTodayStats(analyticsService.calculateAnalytics(userId, "daily"));
        dashboard.setWeekStats(analyticsService.calculateAnalytics(userId, "weekly"));
        dashboard.setMonthStats(analyticsService.calculateAnalytics(userId, "monthly"));
        
        // Graphiques (données simulées pour démo - à remplacer par vraies données)
        dashboard.setProgressionChart(generateProgressionChart(userId));
        dashboard.setAccuracyChart(generateAccuracyChart(userId));
        dashboard.setActivityChart(generateActivityChart(userId));
        
        // Top catégories
        AnalyticsDTO monthStats = dashboard.getMonthStats();
        if (monthStats.getQuizzesByCategory() != null) {
            List<DashboardResponse.TopCategory> topCategories = monthStats.getQuizzesByCategory().entrySet().stream()
                .map(entry -> {
                    String category = entry.getKey();
                    int count = entry.getValue();
                    double avgScore = monthStats.getScoresByCategory().getOrDefault(category, 0.0);
                    return new DashboardResponse.TopCategory(category, count, avgScore);
                })
                .sorted((a, b) -> Integer.compare(b.getQuizCount(), a.getQuizCount()))
                .limit(5)
                .collect(Collectors.toList());
            dashboard.setTopCategories(topCategories);
        }
        
        // Achievements récents
        List<Achievement> achievements = gamificationService.getUserAchievements(userId);
        List<String> recentAchievements = achievements.stream()
            .map(Achievement::getName)
            .limit(5)
            .collect(Collectors.toList());
        dashboard.setRecentAchievements(recentAchievements);
        
        // Recommandations
        dashboard.setRecommendations(generateRecommendations(progress, monthStats));
        
        // KPIs clés
        dashboard.setKeyMetrics(analyticsService.calculateKPIs(userId));
        
        return ResponseEntity.ok(dashboard);
    }
    
    /**
     * Analytics par période
     */
    @GetMapping("/{userId}/analytics")
    @Operation(summary = "Analytics par période", 
               description = "Récupère les analytics pour une période spécifique")
    public ResponseEntity<AnalyticsDTO> getAnalytics(
            @PathVariable String userId,
            @RequestParam(defaultValue = "weekly") String period) {
        
        AnalyticsDTO analytics = analyticsService.calculateAnalytics(userId, period);
        return ResponseEntity.ok(analytics);
    }
    
    /**
     * Analytics globales (tous les utilisateurs)
     */
    @GetMapping("/global")
    @Operation(summary = "Analytics globales", 
               description = "Statistiques globales de la plateforme")
    public ResponseEntity<Map<String, Object>> getGlobalAnalytics() {
        Map<String, Object> global = analyticsService.getGlobalAnalytics();
        return ResponseEntity.ok(global);
    }
    
    /**
     * KPIs clés d'un utilisateur
     */
    @GetMapping("/{userId}/kpis")
    @Operation(summary = "KPIs utilisateur", 
               description = "Indicateurs clés de performance")
    public ResponseEntity<Map<String, Object>> getUserKPIs(@PathVariable String userId) {
        Map<String, Object> kpis = analyticsService.calculateKPIs(userId);
        return ResponseEntity.ok(kpis);
    }
    
    /**
     * Comparaison avec d'autres utilisateurs
     */
    @GetMapping("/{userId}/compare")
    @Operation(summary = "Comparaison avec pairs", 
               description = "Compare les performances avec la moyenne")
    public ResponseEntity<Map<String, Object>> compareWithPeers(@PathVariable String userId) {
        Map<String, Object> comparison = new HashMap<>();
        
        UserProgress userProgress = gamificationService.getUserProgress(userId);
        Map<String, Object> globalStats = analyticsService.getGlobalAnalytics();
        
        comparison.put("userAccuracy", userProgress.getAverageScore());
        comparison.put("averageAccuracy", globalStats.get("averageAccuracy"));
        comparison.put("userLevel", userProgress.getLevel());
        
        // Calculer percentile
        List<UserProgress> allUsers = gamificationService.getTopPlayers(1000);
        long betterThan = allUsers.stream()
            .filter(p -> p.getLevel() < userProgress.getLevel())
            .count();
        int percentile = (int) ((double) betterThan / allUsers.size() * 100);
        comparison.put("percentile", percentile);
        
        return ResponseEntity.ok(comparison);
    }
    
    /**
     * Génère les données du graphique de progression
     */
    private List<DashboardResponse.ChartData> generateProgressionChart(String userId) {
        List<DashboardResponse.ChartData> data = new ArrayList<>();
        // À implémenter : récupérer l'historique réel de XP
        // Pour l'instant, données simulées
        for (int i = 7; i >= 0; i--) {
            String date = java.time.LocalDate.now().minusDays(i).toString();
            data.add(new DashboardResponse.ChartData("XP", (7 - i) * 100 + 50, date));
        }
        return data;
    }
    
    /**
     * Génère les données du graphique de précision
     */
    private List<DashboardResponse.ChartData> generateAccuracyChart(String userId) {
        List<DashboardResponse.ChartData> data = new ArrayList<>();
        // Données simulées
        Random random = new Random();
        for (int i = 7; i >= 0; i--) {
            String date = java.time.LocalDate.now().minusDays(i).toString();
            double accuracy = 70 + random.nextInt(30);
            data.add(new DashboardResponse.ChartData("Accuracy", accuracy, date));
        }
        return data;
    }
    
    /**
     * Génère les données du graphique d'activité
     */
    private List<DashboardResponse.ChartData> generateActivityChart(String userId) {
        List<DashboardResponse.ChartData> data = new ArrayList<>();
        // Données simulées
        Random random = new Random();
        for (int i = 7; i >= 0; i--) {
            String date = java.time.LocalDate.now().minusDays(i).toString();
            int quizCount = random.nextInt(5);
            data.add(new DashboardResponse.ChartData("Quiz", quizCount, date));
        }
        return data;
    }
    
    /**
     * Génère des recommandations personnalisées
     */
    private List<String> generateRecommendations(UserProgress progress, AnalyticsDTO monthStats) {
        List<String> recommendations = new ArrayList<>();
        
        if (progress.getCurrentStreak() == 0) {
            recommendations.add("💡 Commencez une série ! Complétez un quiz aujourd'hui.");
        }
        
        if (progress.getAverageScore() < 70) {
            recommendations.add("📚 Revoyez les bases pour améliorer votre score moyen.");
        }
        
        if (progress.getTotalQuizCompleted() < 5) {
            recommendations.add("🎯 Objectif : Complétez 5 quiz pour débloquer un achievement !");
        }
        
        if (monthStats.getQuizCompleted() < 10) {
            recommendations.add("🚀 Augmentez votre activité ce mois-ci pour monter de niveau.");
        }
        
        if (progress.getUnlockedAchievements().size() < 5) {
            recommendations.add("🏆 Débloquez plus d'achievements pour gagner des bonus XP.");
        }
        
        if (recommendations.isEmpty()) {
            recommendations.add("🌟 Excellent travail ! Continuez sur votre lancée.");
        }
        
        return recommendations;
    }
}