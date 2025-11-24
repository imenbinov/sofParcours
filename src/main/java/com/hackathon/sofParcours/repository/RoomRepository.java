package com.hackathon.sofParcours.repository;

import com.hackathon.sofParcours.model.Room;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends MongoRepository<Room, String> {
    Optional<Room> findByCode(String code);

    /**
     * Recherche une Room par son slug normalisé
     * Utilisé pour l'idempotence du endpoint search-or-create
     */
    Optional<Room> findBySlug(String slug);
}
