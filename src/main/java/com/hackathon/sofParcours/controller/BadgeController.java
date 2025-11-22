package com.hackathon.sofParcours.controller;

import com.hackathon.sofParcours.model.Badge;
import com.hackathon.sofParcours.service.BadgeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/badges")
@Tag(name = "Badges", description = "Gestion des badges")
public class BadgeController {

    private final BadgeService badgeService;

    public BadgeController(BadgeService badgeService) {
        this.badgeService = badgeService;
    }

    @Operation(summary = "Lister tous les badges disponibles")
    @GetMapping
    public ResponseEntity<List<Badge>> getAllBadges() {
        List<Badge> badges = badgeService.getAllBadges();
        return ResponseEntity.ok(badges);
    }

    @Operation(summary = "Obtenir les badges d'un utilisateur")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Badge>> getUserBadges(@PathVariable String userId) {
        List<Badge> badges = badgeService.getUserBadges(userId);
        return ResponseEntity.ok(badges);
    }
}
