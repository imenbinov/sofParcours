package com.hackathon.sofParcours.service;

import com.hackathon.sofParcours.model.Notification;
import com.hackathon.sofParcours.model.Achievement;
import com.hackathon.sofParcours.model.UserProgress;
import com.hackathon.sofParcours.repository.NotificationRepository;
import com.hackathon.sofParcours.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service de gestion des notifications
 */
@Service
public class NotificationService {
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EmailService emailService;
    
    /**
     * Crée une notification pour un utilisateur
     */
    public Notification createNotification(String userId, String type, String title, String message, String icon) {
        Notification notification = new Notification(userId, type, title, message);
        notification.setIcon(icon);
        notification.setExpiresAt(LocalDateTime.now().plusDays(30)); // Expire après 30 jours
        
        return notificationRepository.save(notification);
    }
    
    /**
     * Notifie un achievement débloqué
     */
    public void notifyAchievementUnlocked(String userId, Achievement achievement) {
        String title = "🏆 Achievement débloqué !";
        String message = String.format("Félicitations ! Vous avez débloqué '%s'", achievement.getName());
        
        Notification notification = createNotification(userId, "ACHIEVEMENT", title, message, "🏆");
        notification.setPriority("HIGH");
        notification.setRelatedEntityId(achievement.getId());
        notification.setRelatedEntityType("ACHIEVEMENT");
        notification.setActionUrl("/achievements/" + achievement.getId());
        
        notificationRepository.save(notification);
        
        // Envoyer email si l'achievement est EPIC ou LEGENDARY
        if ("EPIC".equals(achievement.getRarity()) || "LEGENDARY".equals(achievement.getRarity())) {
            userRepository.findById(userId).ifPresent(user -> {
                emailService.sendAchievementEmail(user.getEmail(), user.getUsername(), achievement.getName());
            });
        }
    }
    
    /**
     * Notifie un level up
     */
    public void notifyLevelUp(String userId, int newLevel, String newTitle) {
        String title = "🎊 Niveau supérieur !";
        String message = String.format("Bravo ! Vous êtes maintenant niveau %d - %s", newLevel, newTitle);
        
        Notification notification = createNotification(userId, "LEVEL_UP", title, message, "🎊");
        notification.setPriority("HIGH");
        
        notificationRepository.save(notification);
        
        // Envoyer email pour les niveaux importants (5, 10, 25, 50)
        if (newLevel % 5 == 0) {
            userRepository.findById(userId).ifPresent(user -> {
                emailService.sendLevelUpEmail(user.getEmail(), user.getUsername(), newLevel, newTitle);
            });
        }
    }
    
    /**
     * Notifie une série en danger (risque de perdre le streak)
     */
    public void notifyStreakAtRisk(String userId, int currentStreak) {
        String title = "🔥 Votre série est en danger !";
        String message = String.format("Vous avez une série de %d jours ! Ne la perdez pas, complétez un quiz aujourd'hui.", currentStreak);
        
        Notification notification = createNotification(userId, "STREAK", title, message, "🔥");
        notification.setPriority("URGENT");
        notification.setActionUrl("/quiz");
        
        notificationRepository.save(notification);
    }
    
    /**
     * Rappel d'inactivité
     */
    public void notifyInactiveUser(String userId, int daysInactive) {
        String title = "⏰ Nous vous avons manqué !";
        String message = String.format("Cela fait %d jours ! Revenez pour continuer votre progression.", daysInactive);
        
        Notification notification = createNotification(userId, "REMINDER", title, message, "⏰");
        notification.setPriority("MEDIUM");
        notification.setActionUrl("/dashboard");
        
        notificationRepository.save(notification);
        
        // Envoyer email après 7 jours d'inactivité
        if (daysInactive >= 7) {
            userRepository.findById(userId).ifPresent(user -> {
                emailService.sendActivityReminder(user.getEmail(), user.getUsername(), daysInactive);
            });
        }
    }
    
    /**
     * Notification de nouveau quiz disponible
     */
    public void notifyNewQuizAvailable(String userId, String quizTitle, String quizId) {
        String title = "📚 Nouveau quiz disponible !";
        String message = String.format("Découvrez le nouveau quiz : %s", quizTitle);
        
        Notification notification = createNotification(userId, "SYSTEM", title, message, "📚");
        notification.setPriority("LOW");
        notification.setRelatedEntityId(quizId);
        notification.setRelatedEntityType("QUIZ");
        notification.setActionUrl("/quiz/" + quizId);
        
        notificationRepository.save(notification);
    }
    
    /**
     * Notification de défi d'équipe
     */
    public void notifyChallengeInvite(String userId, String challengeName, String challengeId) {
        String title = "⚔️ Invitation à un défi !";
        String message = String.format("Vous êtes invité au défi : %s", challengeName);
        
        Notification notification = createNotification(userId, "CHALLENGE", title, message, "⚔️");
        notification.setPriority("HIGH");
        notification.setRelatedEntityId(challengeId);
        notification.setRelatedEntityType("CHALLENGE");
        notification.setActionUrl("/challenges/" + challengeId);
        
        notificationRepository.save(notification);
    }
    
    /**
     * Récupère toutes les notifications d'un utilisateur
     */
    public List<Notification> getUserNotifications(String userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
    
    /**
     * Récupère les notifications non lues
     */
    public List<Notification> getUnreadNotifications(String userId) {
        return notificationRepository.findByUserIdAndRead(userId, false);
    }
    
    /**
     * Compte les notifications non lues
     */
    public long countUnreadNotifications(String userId) {
        return notificationRepository.countByUserIdAndRead(userId, false);
    }
    
    /**
     * Marque une notification comme lue
     */
    public void markAsRead(String notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }
    
    /**
     * Marque toutes les notifications comme lues
     */
    public void markAllAsRead(String userId) {
        List<Notification> unread = getUnreadNotifications(userId);
        unread.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(unread);
    }
    
    /**
     * Supprime une notification
     */
    public void deleteNotification(String notificationId) {
        notificationRepository.deleteById(notificationId);
    }
    
    /**
     * Supprime les notifications expirées
     */
    public void deleteExpiredNotifications() {
        notificationRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    }
    
    /**
     * Calcule le temps écoulé ("il y a 2 heures")
     */
    public String getTimeAgo(LocalDateTime dateTime) {
        long minutes = ChronoUnit.MINUTES.between(dateTime, LocalDateTime.now());
        
        if (minutes < 1) return "À l'instant";
        if (minutes < 60) return "Il y a " + minutes + " min";
        
        long hours = minutes / 60;
        if (hours < 24) return "Il y a " + hours + " h";
        
        long days = hours / 24;
        if (days < 7) return "Il y a " + days + " j";
        
        long weeks = days / 7;
        if (weeks < 4) return "Il y a " + weeks + " sem";
        
        long months = days / 30;
        return "Il y a " + months + " mois";
    }
}