package com.hackathon.sofParcours.service;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service pour l'envoi d'emails
 * Note: Configuration SMTP requise dans application.properties
 */
@Service
public class EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    // Pour l'instant, simulation d'envoi d'email (logs)
    // Pour production: utiliser JavaMailSender de Spring Boot
    
    /**
     * Envoie un email de notification
     */
    public void sendNotificationEmail(String to, String subject, String body) {
        logger.info("📧 EMAIL envoyé à {} - Sujet: {}", to, subject);
        logger.debug("Corps du message: {}", body);
        
        // TODO: Implémenter l'envoi réel avec JavaMailSender
        // Example:
        // MimeMessage message = mailSender.createMimeMessage();
        // MimeMessageHelper helper = new MimeMessageHelper(message);
        // helper.setTo(to);
        // helper.setSubject(subject);
        // helper.setText(body, true);
        // mailSender.send(message);
    }
    
    /**
     * Envoie un email de bienvenue
     */
    public void sendWelcomeEmail(String to, String username) {
        String subject = "🎉 Bienvenue sur SofParcours !";
        String body = String.format(
            "Bonjour %s,\n\n" +
            "Bienvenue sur SofParcours ! 🚀\n\n" +
            "Commencez votre aventure d'apprentissage dès maintenant :\n" +
            "- Complétez votre premier quiz\n" +
            "- Gagnez des XP et montez de niveau\n" +
            "- Débloquez des achievements\n\n" +
            "Bonne chance !\n" +
            "L'équipe SofParcours",
            username
        );
        
        sendNotificationEmail(to, subject, body);
    }
    
    /**
     * Envoie un rappel d'activité
     */
    public void sendActivityReminder(String to, String username, int daysInactive) {
        String subject = "⏰ Nous vous avons manqué !";
        String body = String.format(
            "Bonjour %s,\n\n" +
            "Cela fait %d jours que vous n'avez pas visité SofParcours. 😢\n\n" +
            "Ne perdez pas votre streak ! Revenez dès maintenant pour :\n" +
            "- Maintenir votre série de jours consécutifs\n" +
            "- Découvrir de nouveaux quiz\n" +
            "- Grimper dans le leaderboard\n\n" +
            "À bientôt !\n" +
            "L'équipe SofParcours",
            username, daysInactive
        );
        
        sendNotificationEmail(to, subject, body);
    }
    
    /**
     * Envoie une notification d'achievement débloqué
     */
    public void sendAchievementEmail(String to, String username, String achievementName) {
        String subject = "🏆 Nouvel Achievement débloqué !";
        String body = String.format(
            "Félicitations %s ! 🎉\n\n" +
            "Vous venez de débloquer l'achievement :\n" +
            "🏆 %s\n\n" +
            "Continuez comme ça !\n\n" +
            "L'équipe SofParcours",
            username, achievementName
        );
        
        sendNotificationEmail(to, subject, body);
    }
    
    /**
     * Envoie un rapport hebdomadaire
     */
    public void sendWeeklyReport(String to, String username, int quizCompleted, int xpGained, int rank) {
        String subject = "📊 Votre rapport hebdomadaire SofParcours";
        String body = String.format(
            "Bonjour %s,\n\n" +
            "Voici votre bilan de la semaine :\n\n" +
            "✅ Quiz complétés : %d\n" +
            "⭐ XP gagnés : %d\n" +
            "🏅 Rang : #%d\n\n" +
            "Objectif de la semaine prochaine : dépassez-vous !\n\n" +
            "L'équipe SofParcours",
            username, quizCompleted, xpGained, rank
        );
        
        sendNotificationEmail(to, subject, body);
    }
    
    /**
     * Envoie une notification de level up
     */
    public void sendLevelUpEmail(String to, String username, int newLevel, String title) {
        String subject = "🎊 Niveau supérieur atteint !";
        String body = String.format(
            "Bravo %s ! 🎊\n\n" +
            "Vous venez d'atteindre le niveau %d !\n" +
            "Nouveau titre : %s\n\n" +
            "Continuez votre ascension vers le sommet !\n\n" +
            "L'équipe SofParcours",
            username, newLevel, title
        );
        
        sendNotificationEmail(to, subject, body);
    }
}