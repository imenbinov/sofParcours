package com.hackathon.sofParcours.controller;

import com.hackathon.sofParcours.model.Room;
import com.hackathon.sofParcours.service.RoomService;
import com.hackathon.sofParcours.service.RoomCreationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "*")
public class RoomController {

    private final RoomService roomService;
    private final RoomCreationService roomCreationService;

    public RoomController(RoomService roomService, RoomCreationService roomCreationService) {
        this.roomService = roomService;
        this.roomCreationService = roomCreationService;
    }

    /**
     * Récupérer toutes les rooms
     */
    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        List<Room> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(rooms);
    }

    /**
     * Récupérer une room par son code
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<Room> getRoomByCode(@PathVariable String code) {
        Room room = roomService.getRoomByCode(code);
        return ResponseEntity.ok(room);
    }

    /**
     * Récupérer une room par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable String id) {
        Room room = roomService.getRoomById(id);
        return ResponseEntity.ok(room);
    }

    /**
     * Créer une nouvelle room
     */
    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        String description = request.get("description");
        String createdBy = request.getOrDefault("createdBy", "anonymous");

        Room room = roomService.createRoom(name, description, createdBy);
        return ResponseEntity.ok(room);
    }

    /**
     * Rejoindre une room
     */
    @PostMapping("/{code}/join")
    public ResponseEntity<Room> joinRoom(@PathVariable String code, @RequestBody Map<String, String> request) {
        String userId = request.getOrDefault("userId", "anonymous");
        Room room = roomService.joinRoom(code, userId);
        return ResponseEntity.ok(room);
    }

    /**
     * Mettre à jour le statut d'une room
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Room> updateRoomStatus(@PathVariable String id, @RequestBody Map<String, String> request) {
        String status = request.get("status");
        Room room = roomService.updateRoomStatus(id, status);
        return ResponseEntity.ok(room);
    }

    /**
     * Rechercher ou créer une room avec génération IA
     * Endpoint idempotent qui :
     * 1. Normalise la requête en slug
     * 2. Recherche si une Room existe déjà
     * 3. Si non, génère via IA et sauvegarde
     * 4. Retourne la Room complète (avec Quiz + Questions)
     */
    @GetMapping("/search-or-create")
    public ResponseEntity<?> searchOrCreate(
            @RequestParam("q") String query,
            @RequestParam(value = "userProfile", required = false, defaultValue = "anonymous") String userProfile) {
        
        try {
            // Validation de la requête
            if (query == null || query.trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Query parameter 'q' is required and cannot be empty");
                return ResponseEntity.badRequest().body(error);
            }

            // Appel au service de création/recherche
            Room room = roomCreationService.findOrCreateByQuery(query, userProfile);
            
            // Retour de la Room complète avec Quiz et Questions
            return ResponseEntity.ok(room);
            
        } catch (IllegalArgumentException e) {
            // Erreur de validation
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
            
        } catch (RuntimeException e) {
            // Erreur de génération IA ou autre erreur serveur
            if (e.getMessage() != null && e.getMessage().contains("AI generation failed")) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "AI generation failed");
                error.put("details", e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(error);
            }
            
            // Autres erreurs serveur
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error");
            error.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
