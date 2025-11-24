package com.hackathon.sofParcours.service;

import com.hackathon.sofParcours.model.Room;
import com.hackathon.sofParcours.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service de consultation des Rooms
 * IMPORTANT: La création de Rooms se fait UNIQUEMENT via RoomCreationService avec l'IA
 */
@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    /**
     * Liste toutes les rooms
     */
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    /**
     * Récupère une room par son code
     */
    public Optional<Room> getRoomByCode(String code) {
        return roomRepository.findByCode(code);
    }

    /**
     * Récupère une room par son ID
     */
    public Optional<Room> getRoomById(String id) {
        return roomRepository.findById(id);
    }
}
