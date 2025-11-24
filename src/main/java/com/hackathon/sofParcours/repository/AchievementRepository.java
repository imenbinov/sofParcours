package com.hackathon.sofParcours.repository;

import com.hackathon.sofParcours.model.Achievement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AchievementRepository extends MongoRepository<Achievement, String> {
    
    List<Achievement> findByCategory(String category);
    
    List<Achievement> findByRarity(String rarity);
    
    List<Achievement> findByIsSecret(boolean isSecret);
    
    List<Achievement> findByCategoryAndRequiredValueLessThanEqual(String category, int value);
}