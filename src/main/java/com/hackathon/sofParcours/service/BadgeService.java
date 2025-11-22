package com.hackathon.sofParcours.service;

import com.hackathon.sofParcours.model.Badge;
import com.hackathon.sofParcours.model.User;
import com.hackathon.sofParcours.repository.BadgeRepository;
import com.hackathon.sofParcours.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final UserRepository userRepository;

    public BadgeService(BadgeRepository badgeRepository, UserRepository userRepository) {
        this.badgeRepository = badgeRepository;
        this.userRepository = userRepository;
    }

    public List<String> checkAndAwardBadges(String userId, int totalScore, int correctAnswers) {
        List<String> badgesEarned = new ArrayList<>();

        // Badge "Première victoire" - Premier quiz complété
        if (correctAnswers > 0) {
            Badge firstWinBadge = getOrCreateBadge("first-win", "Première victoire", "Compléter son premier quiz", 0);
            badgesEarned.add(firstWinBadge.getId());
        }

        // Badge "Expert" - 10 bonnes réponses d'affilée
        if (correctAnswers >= 10) {
            Badge expertBadge = getOrCreateBadge("expert", "Expert", "10 bonnes réponses d'affilée", 100);
            badgesEarned.add(expertBadge.getId());
        }

        // Badge "Champion" - Score total > 500
        if (totalScore >= 500) {
            Badge championBadge = getOrCreateBadge("champion", "Champion", "Score total supérieur à 500", 500);
            badgesEarned.add(championBadge.getId());
        }

        // Mise à jour du profil utilisateur
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            List<Badge> currentBadges = user.getBadges() != null ? user.getBadges() : new ArrayList<>();
            badgesEarned.forEach(badgeId -> {
                Badge badge = badgeRepository.findById(badgeId).orElse(null);
                if (badge != null && !currentBadges.contains(badge)) {
                    currentBadges.add(badge);
                }
            });
            user.setBadges(currentBadges);
            user.setTotalScore(user.getTotalScore() + totalScore);
            userRepository.save(user);
        }

        return badgesEarned;
    }

    private Badge getOrCreateBadge(String id, String name, String description, int pointsRequired) {
        return badgeRepository.findById(id).orElseGet(() -> {
            Badge badge = new Badge();
            badge.setId(id);
            badge.setName(name);
            badge.setDescription(description);
            badge.setPointsRequired(pointsRequired);
            return badgeRepository.save(badge);
        });
    }

    public List<Badge> getAllBadges() {
        return badgeRepository.findAll();
    }

    public List<Badge> getUserBadges(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        return user != null && user.getBadges() != null ? user.getBadges() : new ArrayList<>();
    }
}
