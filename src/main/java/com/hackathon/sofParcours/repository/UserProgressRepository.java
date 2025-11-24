package com.hackathon.sofParcours.repository;

import com.hackathon.sofParcours.model.UserProgress;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserProgressRepository extends MongoRepository<UserProgress, String> {
    
    Optional<UserProgress> findByUserId(String userId);
    
    List<UserProgress> findTop10ByOrderByLevelDescCurrentXPDesc();
    
    List<UserProgress> findByLevelGreaterThanEqual(int level);
    
    List<UserProgress> findByCurrentStreakGreaterThanEqual(int streak);
}