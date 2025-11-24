package com.hackathon.sofParcours.controller;

import com.hackathon.sofParcours.model.Room;
import com.hackathon.sofParcours.service.RoomCreationService;
import com.hackathon.sofParcours.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller pour la consultation des Rooms
 * IMPORTANT: La création de Rooms se fait UNIQUEMENT via /search-or-create avec l'IA
 */
@RestController
@RequestMapping("/api/rooms")
@Tag(name = "🏠 Rooms", description = "Consultation des salles de quiz (Création via IA uniquement)")
@CrossOrigin(origins = "*")
public class RoomController {

    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    private final RoomService roomService;
    private final RoomCreationService roomCreationService;

    public RoomController(RoomService roomService, RoomCreationService roomCreationService) {
        this.roomService = roomService;
        this.roomCreationService = roomCreationService;
    }

    /**
     * ✨ ENDPOINT PRINCIPAL - Recherche ou crée une Room avec IA
     * C'est la SEULE façon de créer une Room dans l'application
     */
    @Operation(
            summary = "🤖 Rechercher ou créer automatiquement une Room avec IA",
            description = "**Endpoint idempotent** : " +
                    "- Si une room existe pour le sujet → retourne la room existante\n" +
                    "- Sinon → l'IA génère automatiquement : Room + Quiz + 10 Questions\n\n" +
                    "**Normalisation automatique** : 'DevOps avancé' → slug 'devops-avance'"
    )
    @GetMapping("/search-or-create")
    public ResponseEntity<?> searchOrCreateRoom(
            @Parameter(description = "Sujet du quiz", required = true, example = "DevOps avancé")
            @RequestParam String q,
            @Parameter(description = "Profil utilisateur", example = "developer")
            @RequestParam(defaultValue = "anonymous") String userProfile
    ) {
        if (q == null || q.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Query parameter 'q' is required"));
        }

        try {
            Room room = roomCreationService.findOrCreateByQuery(q, userProfile);
            return ResponseEntity.ok(room);
        } catch (Exception e) {
            logger.error("Failed to search or create room", e);
            return ResponseEntity.status(502).body(Map.of(
                    "error", "AI generation failed",
                    "details", e.getMessage()
            ));
        }
    }

    /**
     * Consultation uniquement - Liste toutes les rooms
     */
    @Operation(summary = "📋 Lister toutes les rooms disponibles")
    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    /**
     * Consultation uniquement - Récupérer une room par code
     */
    @Operation(summary = "🔍 Récupérer une room par son code")
    @GetMapping("/code/{code}")
    public ResponseEntity<?> getRoomByCode(
            @Parameter(description = "Code de la room", example = "ABC123")
            @PathVariable String code
    ) {
        return roomService.getRoomByCode(code)
                .map(room -> ResponseEntity.ok(room))
                .orElse(ResponseEntity.notFound().build());
    }
}
