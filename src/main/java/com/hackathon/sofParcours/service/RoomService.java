package com.hackathon.sofParcours.service;

import com.hackathon.sofParcours.model.Room;
import com.hackathon.sofParcours.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room createRoom(String name, String description, String createdBy) {
        Room room = new Room();
        room.setCode(generateRoomCode());
        room.setName(name);
        room.setDescription(description);
        room.setCreatedBy(createdBy);
        room.setStatus("WAITING");
        room.setCreatedAt(LocalDateTime.now());
        room.setUpdatedAt(LocalDateTime.now());

        return roomRepository.save(room);
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomByCode(String code) {
        return roomRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with code: " + code));
    }

    public Room getRoomById(String id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + id));
    }

    public Room joinRoom(String code, String userId) {
        Room room = getRoomByCode(code);

        if (!room.getParticipantIds().contains(userId)) {
            room.getParticipantIds().add(userId);
            room.setUpdatedAt(LocalDateTime.now());
            roomRepository.save(room);
        }

        return room;
    }

    public Room updateRoomStatus(String roomId, String status) {
        Room room = getRoomById(roomId);
        room.setStatus(status);
        room.setUpdatedAt(LocalDateTime.now());
        return roomRepository.save(room);
    }

    private String generateRoomCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }

        return code.toString();
    }
}
