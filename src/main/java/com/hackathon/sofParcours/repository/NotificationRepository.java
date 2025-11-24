package com.hackathon.sofParcours.repository;

import com.hackathon.sofParcours.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    
    List<Notification> findByUserId(String userId);
    
    List<Notification> findByUserIdAndRead(String userId, boolean read);
    
    List<Notification> findByUserIdOrderByCreatedAtDesc(String userId);
    
    List<Notification> findByUserIdAndType(String userId, String type);
    
    long countByUserIdAndRead(String userId, boolean read);
    
    List<Notification> findByExpiresAtBefore(LocalDateTime date);
    
    void deleteByExpiresAtBefore(LocalDateTime date);
}