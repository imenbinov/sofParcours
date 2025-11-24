package com.hackathon.sofParcours.service;

import com.hackathon.sofParcours.dto.AnalyticsDTO;
import com.hackathon.sofParcours.model.Analytics;
import com.hackathon.sofParcours.model.UserProgress;
import com.hackathon.sofParcours.model.QuizHistory;
import com.hackathon.sofParcours.repository.AnalyticsRepository;
import com.hackathon.sofParcours.repository.UserProgressRepository;
import com.hackathon.sofParcours.repository.QuizHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service pour calculer et gérer les analytics
 */
@Service
public class AnalyticsService {
    
    @Autowired
    private AnalyticsRepository analyticsRepository;
    
    @Autowired
    private UserProgressRepository userProgressRepository;
    
    @Autowired
    private QuizHistoryRepository quizHistoryRepository;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    
    /**
     * Calcule les analytics pour un utilisateur sur une période
     */
    public AnalyticsDTO calculateAnalytics(String userId, String period) {
        LocalDateTime[] range = getPeriodRange(period);
        LocalDateTime start = range[0];
        LocalDateTime end = range[1];
        
        AnalyticsDTO dto = new AnalyticsDTO();
        dto.setPeriod(period);
        dto.setStartDate(start.format(DATE_FORMATTER));
        dto.setEndDate(end.format(DATE_FORMATTER));
        
        // Récupérer les données de progression
        UserProgress progress = userProgressRepository.findByUserId(userId).orElse(null);
        if (progress == null) {
            return dto; // Retourner vide si pas de données
        }
        
        // Récupérer l'historique des quiz pour la période
        List<QuizHistory> quizHistory = quizHistoryRepository.findByUserIdAndCreatedAtBetween(userId, start, end);
        
        // Calculer les métriques
        dto.setQuizCompleted(quizHistory.size());
        
        int totalQuestions = 0;
        int correctAnswers = 0;
        double totalScore = 0;
        int perfectScores = 0;
        Map<String, Integer> categoryCount = new HashMap<>();
        Map<String, List<Double>> categoryScores = new HashMap<>();
        
        for (QuizHistory history : quizHistory) {
            totalQuestions += history.getTotalQuestions();
            correctAnswers += history.getCorrectAnswers();
            
            // Calculer score pour ce quiz
            double score = history.calculateScore();
            totalScore += score;
            
            if (score == 100) {
                perfectScores++;
            }
            
            // Par catégorie (utiliser "Général" par défaut)
            String category = "Général";
            categoryCount.put(category, categoryCount.getOrDefault(category, 0) + 1);
            categoryScores.computeIfAbsent(category, k -> new ArrayList<>()).add(score);
        }
        
        dto.setQuestionsAnswered(totalQuestions);
        
        if (totalQuestions > 0) {
            double accuracy = (double) correctAnswers / totalQuestions * 100;
            dto.setAccuracyRate(Math.round(accuracy * 100.0) / 100.0);
        }
        
        if (!quizHistory.isEmpty()) {
            double avgScore = totalScore / quizHistory.size();
            dto.setAverageScore(Math.round(avgScore * 100.0) / 100.0);
        }
        
        dto.setPerfectScores(perfectScores);
        dto.setQuizzesByCategory(categoryCount);
        
        // Calculer scores moyens par catégorie
        Map<String, Double> avgScoresByCategory = new HashMap<>();
        for (Map.Entry<String, List<Double>> entry : categoryScores.entrySet()) {
            double avg = entry.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0);
            avgScoresByCategory.put(entry.getKey(), Math.round(avg * 100.0) / 100.0);
        }
        dto.setScoresByCategory(avgScoresByCategory);
        
        // XP et niveaux gagnés (estimation basée sur la période)
        dto.setXpGained(progress.getCurrentXP());
        dto.setLevelsGained(progress.getLevel());
        dto.setAchievementsUnlocked(progress.getUnlockedAchievements().size());
        
        // Calcul du trend (comparaison avec période précédente)
        calculateTrend(dto, userId, period);
        
        return dto;
    }
    
    /**
     * Calcule le trend en comparant avec la période précédente
     */
    private void calculateTrend(AnalyticsDTO current, String userId, String period) {
        LocalDateTime[] previousRange = getPreviousPeriodRange(period);
        List<QuizHistory> previousHistory = quizHistoryRepository.findByUserIdAndCreatedAtBetween(
            userId, previousRange[0], previousRange[1]
        );
        
        int previousCount = previousHistory.size();
        int currentCount = current.getQuizCompleted();
        
        if (previousCount > 0) {
            double growth = ((double) (currentCount - previousCount) / previousCount) * 100;
            current.setGrowthRate(Math.round(growth * 100.0) / 100.0);
            
            if (growth > 5) current.setTrend("up");
            else if (growth < -5) current.setTrend("down");
            else current.setTrend("stable");
        } else {
            current.setTrend(currentCount > 0 ? "up" : "stable");
        }
    }
    
    /**
     * Sauvegarde les analytics dans la base
     */
    public Analytics saveAnalytics(String userId, String type) {
        Analytics analytics = analyticsRepository.findByUserIdAndType(userId, type)
            .orElse(new Analytics(userId, type));
        
        LocalDateTime[] range = getPeriodRange(type.toLowerCase());
        analytics.setPeriodStart(range[0]);
        analytics.setPeriodEnd(range[1]);
        
        UserProgress progress = userProgressRepository.findByUserId(userId).orElse(null);
        if (progress != null) {
            analytics.setQuizCompleted(progress.getTotalQuizCompleted());
            analytics.setQuestionsAnswered(progress.getTotalQuestionsAnswered());
            analytics.setCorrectAnswers(progress.getCorrectAnswersCount());
            analytics.setAccuracyRate(progress.getAverageScore());
            analytics.setPerfectScores(progress.getPerfectScoresCount());
            analytics.setXpGained(progress.getCurrentXP());
            analytics.setStreakDays(progress.getCurrentStreak());
            analytics.setAchievementsUnlocked(progress.getUnlockedAchievements().size());
        }
        
        analytics.setUpdatedAt(LocalDateTime.now());
        return analyticsRepository.save(analytics);
    }
    
    /**
     * Récupère les analytics globales (tous les utilisateurs)
     */
    public Map<String, Object> getGlobalAnalytics() {
        Map<String, Object> global = new HashMap<>();
        
        long totalUsers = userProgressRepository.count();
        List<UserProgress> allProgress = userProgressRepository.findAll();
        
        int totalQuizCompleted = allProgress.stream()
            .mapToInt(UserProgress::getTotalQuizCompleted)
            .sum();
        
        int totalQuestionsAnswered = allProgress.stream()
            .mapToInt(UserProgress::getTotalQuestionsAnswered)
            .sum();
        
        double avgAccuracy = allProgress.stream()
            .mapToDouble(UserProgress::getAverageScore)
            .average()
            .orElse(0);
        
        global.put("totalUsers", totalUsers);
        global.put("totalQuizCompleted", totalQuizCompleted);
        global.put("totalQuestionsAnswered", totalQuestionsAnswered);
        global.put("averageAccuracy", Math.round(avgAccuracy * 100.0) / 100.0);
        global.put("activeUsers", allProgress.stream()
            .filter(p -> p.getLastActivityDate() != null && 
                        p.getLastActivityDate().isAfter(LocalDateTime.now().minusDays(7)))
            .count());
        
        return global;
    }
    
    /**
     * Calcule les KPIs clés
     */
    public Map<String, Object> calculateKPIs(String userId) {
        Map<String, Object> kpis = new HashMap<>();
        
        UserProgress progress = userProgressRepository.findByUserId(userId).orElse(null);
        if (progress == null) {
            return kpis;
        }
        
        // Engagement Score (0-100)
        int engagementScore = calculateEngagementScore(progress);
        kpis.put("engagementScore", engagementScore);
        
        // Mastery Level (0-100)
        int masteryLevel = (int) progress.getAverageScore();
        kpis.put("masteryLevel", masteryLevel);
        
        // Completion Rate
        double completionRate = progress.getTotalQuizCompleted() > 0 ? 
            (double) progress.getPerfectScoresCount() / progress.getTotalQuizCompleted() * 100 : 0;
        kpis.put("completionRate", Math.round(completionRate * 100.0) / 100.0);
        
        // Learning Velocity (XP par jour)
        int daysActive = progress.getCurrentStreak() > 0 ? progress.getCurrentStreak() : 1;
        double velocity = (double) progress.getCurrentXP() / daysActive;
        kpis.put("learningVelocity", Math.round(velocity * 100.0) / 100.0);
        
        return kpis;
    }
    
    /**
     * Calcule le score d'engagement (0-100)
     */
    private int calculateEngagementScore(UserProgress progress) {
        int score = 0;
        
        // Streak (max 30 points)
        score += Math.min(progress.getCurrentStreak() * 3, 30);
        
        // Quiz complétés (max 30 points)
        score += Math.min(progress.getTotalQuizCompleted() * 2, 30);
        
        // Achievements (max 20 points)
        score += Math.min(progress.getUnlockedAchievements().size() * 2, 20);
        
        // Niveau (max 20 points)
        score += Math.min(progress.getLevel() * 2, 20);
        
        return Math.min(score, 100);
    }
    
    /**
     * Obtient le range de dates pour une période
     */
    private LocalDateTime[] getPeriodRange(String period) {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start;
        
        switch (period.toLowerCase()) {
            case "daily":
            case "today":
                start = end.toLocalDate().atStartOfDay();
                break;
            case "weekly":
            case "week":
                start = end.minusWeeks(1);
                break;
            case "monthly":
            case "month":
                start = end.minusMonths(1);
                break;
            default:
                start = end.minusWeeks(1);
        }
        
        return new LocalDateTime[]{start, end};
    }
    
    /**
     * Obtient le range de la période précédente
     */
    private LocalDateTime[] getPreviousPeriodRange(String period) {
        LocalDateTime[] current = getPeriodRange(period);
        long duration = java.time.Duration.between(current[0], current[1]).toDays();
        
        LocalDateTime previousEnd = current[0];
        LocalDateTime previousStart = previousEnd.minusDays(duration);
        
        return new LocalDateTime[]{previousStart, previousEnd};
    }
}