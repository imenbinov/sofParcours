package com.hackathon.sofParcours.controller;

import com.hackathon.sofParcours.model.Room;
import com.hackathon.sofParcours.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "*")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
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
}
