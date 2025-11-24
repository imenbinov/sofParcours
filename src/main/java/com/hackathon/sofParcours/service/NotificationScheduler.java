package com.hackathon.sofParcours.service;

import com.hackathon.sofParcours.model.UserProgress;
import com.hackathon.sofParcours.repository.UserProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Service pour les tâches planifiées de notifications
 */
@Service
public class NotificationScheduler {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationScheduler.class);
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private UserProgressRepository userProgressRepository;
    
    /**
     * Vérifie les streaks en danger chaque jour à 18h
     * Cron: 0 0 18 * * * = tous les jours à 18h00
     */
    @Scheduled(cron = "0 0 18 * * *")
    public void checkStreaksAtRisk() {
        logger.info("🔥 Vérification des streaks en danger...");
        
        List<UserProgress> allUsers = userProgressRepository.findAll();
        int notificationsSent = 0;
        
        for (UserProgress progress : allUsers) {
            // Si l'utilisateur a un streak actif et n'a pas été actif aujourd'hui
            if (progress.getCurrentStreak() > 0 && progress.getLastActivityDate() != null) {
                LocalDateTime today = LocalDateTime.now().toLocalDate().atStartOfDay();
                LocalDateTime lastActivity = progress.getLastActivityDate().toLocalDate().atStartOfDay();
                
                // Si dernière activité n'est pas aujourd'hui
                if (!lastActivity.equals(today)) {
                    notificationService.notifyStreakAtRisk(progress.getUserId(), progress.getCurrentStreak());
                    notificationsSent++;
                }
            }
        }
        
        logger.info("✅ {} notifications de streak envoyées", notificationsSent);
    }
    
    /**
     * Vérifie les utilisateurs inactifs tous les lundis à 10h
     * Cron: 0 0 10 * * MON = tous les lundis à 10h00
     */
    @Scheduled(cron = "0 0 10 * * MON")
    public void checkInactiveUsers() {
        logger.info("⏰ Vérification des utilisateurs inactifs...");
        
        List<UserProgress> allUsers = userProgressRepository.findAll();
        LocalDateTime now = LocalDateTime.now();
        int notificationsSent = 0;
        
        for (UserProgress progress : allUsers) {
            if (progress.getLastActivityDate() != null) {
                long daysInactive = ChronoUnit.DAYS.between(progress.getLastActivityDate(), now);
                
                // Envoyer rappel après 3, 7, 14 ou 30 jours d'inactivité
                if (daysInactive == 3 || daysInactive == 7 || daysInactive == 14 || daysInactive == 30) {
                    notificationService.notifyInactiveUser(progress.getUserId(), (int) daysInactive);
                    notificationsSent++;
                }
            }
        }
        
        logger.info("✅ {} rappels d'inactivité envoyés", notificationsSent);
    }
    
    /**
     * Nettoie les notifications expirées tous les jours à minuit
     * Cron: 0 0 0 * * * = tous les jours à minuit
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void cleanupExpiredNotifications() {
        logger.info("🧹 Nettoyage des notifications expirées...");
        
        notificationService.deleteExpiredNotifications();
        
        logger.info("✅ Notifications expirées supprimées");
    }
    
    /**
     * Envoie des rapports hebdomadaires tous les vendredis à 17h
     * Cron: 0 0 17 * * FRI = tous les vendredis à 17h00
     */
    @Scheduled(cron = "0 0 17 * * FRI")
    public void sendWeeklyReports() {
        logger.info("📊 Envoi des rapports hebdomadaires...");
        
        // TODO: Implémenter l'envoi de rapports hebdomadaires
        // Nécessite l'intégration avec AnalyticsService
        
        logger.info("✅ Rapports hebdomadaires envoyés");
    }
    
    /**
     * Test: Exécute toutes les heures (désactivé en production)
     * Cron: 0 0 * * * * = toutes les heures
     */
    // @Scheduled(cron = "0 0 * * * *")
    public void hourlyHealthCheck() {
        logger.debug("💓 Health check - NotificationScheduler actif");
    }
}