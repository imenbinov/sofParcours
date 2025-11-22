package com.hackathon.sofParcours.service;

import com.hackathon.sofParcours.model.Room;
import com.hackathon.sofParcours.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room createRoom(String name, String organizerId) {
        Room room = new Room();
        room.setCode(generateUniqueCode());
        room.setName(name);
        room.setOrganizerId(organizerId);
        room.setStatus("WAITING");
        room.setParticipantIds(new ArrayList<>());
        room.getParticipantIds().add(organizerId); // Organizer joins automatically
        room.setCreatedAt(LocalDateTime.now());
        room.setUpdatedAt(LocalDateTime.now());

        return roomRepository.save(room);
    }

    public Room joinRoom(String code, String userId) {
        Room room = roomRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with code: " + code));

        if (room.getParticipantIds().contains(userId)) {
            throw new IllegalArgumentException("User already in room");
        }

        room.getParticipantIds().add(userId);
        room.setUpdatedAt(LocalDateTime.now());

        return roomRepository.save(room);
    }

    public Room getRoomByCode(String code) {
        return roomRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
    }

    private String generateUniqueCode() {
        Random random = new Random();
        String code;
        do {
            code = String.format("%06d", random.nextInt(1000000));
        } while (roomRepository.existsByCode(code));
        return code;
    }
}
