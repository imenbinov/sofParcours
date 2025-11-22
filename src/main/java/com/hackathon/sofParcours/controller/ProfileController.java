package com.hackathon.sofParcours.controller;

import com.hackathon.sofParcours.model.Badge;
import com.hackathon.sofParcours.model.QuizHistory;
import com.hackathon.sofParcours.model.User;
import com.hackathon.sofParcours.repository.QuizHistoryRepository;
import com.hackathon.sofParcours.repository.UserRepository;
import com.hackathon.sofParcours.service.BadgeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
@Tag(name = "Profil", description = "Gestion des profils utilisateurs")
public class ProfileController {

    private final UserRepository userRepository;
    private final BadgeService badgeService;
    private final QuizHistoryRepository quizHistoryRepository;

    public ProfileController(
            UserRepository userRepository,
            BadgeService badgeService,
            QuizHistoryRepository quizHistoryRepository
    ) {
        this.userRepository = userRepository;
        this.badgeService = badgeService;
        this.quizHistoryRepository = quizHistoryRepository;
    }

    @Operation(summary = "Obtenir le profil d'un utilisateur")
    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> getUserProfile(@PathVariable String userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            List<Badge> badges = badgeService.getUserBadges(userId);
            List<QuizHistory> history = quizHistoryRepository.findByUserId(userId);

            Map<String, Object> profile = new HashMap<>();
            profile.put("id", user.getId());
            profile.put("username", user.getUsername());
            profile.put("email", user.getEmail());
            profile.put("totalScore", user.getTotalScore());
            profile.put("badges", badges);
            profile.put("historyCount", history.size());

            return ResponseEntity.ok(profile);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Obtenir l'historique des quiz d'un utilisateur")
    @GetMapping("/{userId}/history")
    public ResponseEntity<List<QuizHistory>> getUserHistory(@PathVariable String userId) {
        List<QuizHistory> history = quizHistoryRepository.findByUserId(userId);
        return ResponseEntity.ok(history);
    }

    @Operation(summary = "Obtenir les badges d'un utilisateur")
    @GetMapping("/{userId}/badges")
    public ResponseEntity<List<Badge>> getUserBadges(@PathVariable String userId) {
        List<Badge> badges = badgeService.getUserBadges(userId);
        return ResponseEntity.ok(badges);
    }
}
