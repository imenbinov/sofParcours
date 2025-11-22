package com.hackathon.sofParcours.config;

import com.hackathon.sofParcours.model.Badge;
import com.hackathon.sofParcours.model.Room;
import com.hackathon.sofParcours.model.User;
import com.hackathon.sofParcours.repository.BadgeRepository;
import com.hackathon.sofParcours.repository.RoomRepository;
import com.hackathon.sofParcours.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

// @Configuration  // Décommentez pour activer l'initialisation automatique
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
            BadgeRepository badgeRepository,
            RoomRepository roomRepository,
            UserRepository userRepository
    ) {
        return args -> {
            // Initialiser les badges si la collection est vide
            if (badgeRepository.count() == 0) {
                System.out.println("Initialisation des badges...");
                
                Badge firstWin = new Badge();
                firstWin.setName("Première Victoire");
                firstWin.setDescription("Remporter votre premier quiz");
                firstWin.setPointsRequired(0);
                firstWin.setIconUrl("🏆");
                badgeRepository.save(firstWin);

                Badge expert = new Badge();
                expert.setName("Expert");
                expert.setDescription("Atteindre 100 points au total");
                expert.setPointsRequired(100);
                expert.setIconUrl("🎓");
                badgeRepository.save(expert);

                Badge champion = new Badge();
                champion.setName("Champion");
                champion.setDescription("Atteindre 500 points au total");
                champion.setPointsRequired(500);
                champion.setIconUrl("👑");
                badgeRepository.save(champion);

                Badge perfectScore = new Badge();
                perfectScore.setName("Score Parfait");
                perfectScore.setDescription("Obtenir 100% de bonnes réponses dans un quiz");
                perfectScore.setPointsRequired(0);
                perfectScore.setIconUrl("⭐");
                badgeRepository.save(perfectScore);

                Badge speedRunner = new Badge();
                speedRunner.setName("Speed Runner");
                speedRunner.setDescription("Répondre à toutes les questions en moins de 30 secondes");
                speedRunner.setPointsRequired(0);
                speedRunner.setIconUrl("⚡");
                badgeRepository.save(speedRunner);

                System.out.println("✅ " + badgeRepository.count() + " badges créés");
            }

            // Initialiser des rooms de démonstration si la collection est vide
            if (roomRepository.count() == 0) {
                System.out.println("Initialisation des rooms de démonstration...");
                
                Room javaRoom = new Room();
                javaRoom.setCode("JAVA01");
                javaRoom.setName("Quiz Java Spring Boot");
                javaRoom.setDescription("Questions sur Java et Spring Boot pour débutants");
                javaRoom.setCreatedBy("admin");
                javaRoom.setStatus("WAITING");
                javaRoom.setCreatedAt(LocalDateTime.now());
                javaRoom.setUpdatedAt(LocalDateTime.now());
                javaRoom.setParticipantIds(new ArrayList<>());
                roomRepository.save(javaRoom);

                Room webRoom = new Room();
                webRoom.setCode("WEB101");
                webRoom.setName("Développement Web Moderne");
                webRoom.setDescription("HTML, CSS, JavaScript et frameworks modernes");
                webRoom.setCreatedBy("admin");
                webRoom.setStatus("WAITING");
                webRoom.setCreatedAt(LocalDateTime.now());
                webRoom.setUpdatedAt(LocalDateTime.now());
                webRoom.setParticipantIds(new ArrayList<>());
                roomRepository.save(webRoom);

                Room pythonRoom = new Room();
                pythonRoom.setCode("PY2024");
                pythonRoom.setName("Python pour Data Science");
                pythonRoom.setDescription("Pandas, NumPy, Machine Learning");
                pythonRoom.setCreatedBy("admin");
                pythonRoom.setStatus("WAITING");
                pythonRoom.setCreatedAt(LocalDateTime.now());
                pythonRoom.setUpdatedAt(LocalDateTime.now());
                pythonRoom.setParticipantIds(new ArrayList<>());
                roomRepository.save(pythonRoom);

                Room dbRoom = new Room();
                dbRoom.setCode("DB2024");
                dbRoom.setName("Bases de Données NoSQL");
                dbRoom.setDescription("MongoDB, Redis, Cassandra");
                dbRoom.setCreatedBy("admin");
                dbRoom.setStatus("WAITING");
                dbRoom.setCreatedAt(LocalDateTime.now());
                dbRoom.setUpdatedAt(LocalDateTime.now());
                dbRoom.setParticipantIds(new ArrayList<>());
                roomRepository.save(dbRoom);

                System.out.println("✅ " + roomRepository.count() + " rooms de démonstration créées");
            }

            // Initialiser un utilisateur de test si la collection est vide
            if (userRepository.count() == 0) {
                System.out.println("Initialisation d'un utilisateur de test...");
                
                User testUser = new User();
                testUser.setUsername("testuser");
                testUser.setEmail("test@sofparcours.com");
                testUser.setPasswordHash("$2a$10$dummyHashForDemoOnly"); // Hash fictif pour démo
                testUser.setRoles(Arrays.asList("USER"));
                testUser.setTotalScore(0);
                testUser.setBadges(new ArrayList<>());
                testUser.setBadgeIds(new ArrayList<>());
                testUser.setCreatedAt(LocalDateTime.now());
                testUser.setLastLoginAt(LocalDateTime.now());
                userRepository.save(testUser);

                User adminUser = new User();
                adminUser.setUsername("admin");
                adminUser.setEmail("admin@sofparcours.com");
                adminUser.setPasswordHash("$2a$10$dummyHashForDemoOnly"); // Hash fictif pour démo
                adminUser.setRoles(Arrays.asList("USER", "ADMIN"));
                adminUser.setTotalScore(0);
                adminUser.setBadges(new ArrayList<>());
                adminUser.setBadgeIds(new ArrayList<>());
                adminUser.setCreatedAt(LocalDateTime.now());
                adminUser.setLastLoginAt(LocalDateTime.now());
                userRepository.save(adminUser);

                System.out.println("✅ " + userRepository.count() + " utilisateurs de test créés");
            }

            System.out.println("\n🎉 Initialisation des données terminée !");
            System.out.println("📊 Base de données prête pour le hackathon SofParcours\n");
        };
    }
}
