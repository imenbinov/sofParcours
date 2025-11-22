package com.hackathon.sofParcours.controller;

import com.hackathon.sofParcours.controller.dto.RoomResponse;
import com.hackathon.sofParcours.model.Room;
import com.hackathon.sofParcours.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/rooms")
@Tag(name = "Rooms", description = "Gestion des salles de quiz")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @Operation(summary = "Créer une nouvelle room", description = "Génère un code unique à 6 chiffres")
    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(@RequestBody Map<String, String> request) {
        try {
            String name = request.get("name");
            String organizerId = request.get("organizerId");

            if (name == null || organizerId == null) {
                return ResponseEntity.badRequest().body(new RoomResponse("Name and organizerId are required"));
            }

            Room room = roomService.createRoom(name, organizerId);
            return ResponseEntity.ok(new RoomResponse(
                    room.getId(),
                    room.getCode(),
                    room.getName(),
                    room.getStatus(),
                    room.getParticipantIds()
            ));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RoomResponse("Error creating room: " + e.getMessage()));
        }
    }

    @Operation(summary = "Rejoindre une room avec un code")
    @PostMapping("/{code}/join")
    public ResponseEntity<RoomResponse> joinRoom(@PathVariable String code, @RequestBody Map<String, String> request) {
        try {
            String userId = request.get("userId");
            if (userId == null) {
                return ResponseEntity.badRequest().body(new RoomResponse("userId is required"));
            }

            Room room = roomService.joinRoom(code, userId);
            return ResponseEntity.ok(new RoomResponse(
                    room.getId(),
                    room.getCode(),
                    room.getName(),
                    room.getStatus(),
                    room.getParticipantIds()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new RoomResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Obtenir les détails d'une room")
    @GetMapping("/{code}")
    public ResponseEntity<RoomResponse> getRoom(@PathVariable String code) {
        try {
            Room room = roomService.getRoomByCode(code);
            return ResponseEntity.ok(new RoomResponse(
                    room.getId(),
                    room.getCode(),
                    room.getName(),
                    room.getStatus(),
                    room.getParticipantIds()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new RoomResponse(e.getMessage()));
        }
    }
}
