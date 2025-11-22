package com.hackathon.sofParcours.repository;

import com.hackathon.sofParcours.model.Badge;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BadgeRepository extends MongoRepository<Badge, String> {
}
