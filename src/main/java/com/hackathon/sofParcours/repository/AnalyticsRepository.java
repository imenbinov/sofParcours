package com.hackathon.sofParcours.repository;

import com.hackathon.sofParcours.model.Analytics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnalyticsRepository extends MongoRepository<Analytics, String> {
    
    Optional<Analytics> findByUserIdAndType(String userId, String type);
    
    List<Analytics> findByUserId(String userId);
    
    List<Analytics> findByType(String type);
    
    List<Analytics> findByUserIdAndPeriodStartBetween(String userId, LocalDateTime start, LocalDateTime end);
    
    List<Analytics> findByPeriodStartBetween(LocalDateTime start, LocalDateTime end);
}