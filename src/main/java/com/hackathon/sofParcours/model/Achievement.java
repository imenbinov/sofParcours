package com.hackathon.sofParcours.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

/**
 * Modèle pour les achievements/succès déblocables
 */
@Document(collection = "achievements")
public class Achievement {
    
    @Id
    private String id;
    private String name;
    private String description;
    private String icon;
    private String category; // QUIZ_MASTER, SPEED_DEMON, PERFECT_SCORE, STREAK_MASTER, SOCIAL_BUTTERFLY
    private int requiredValue; // Valeur nécessaire pour débloquer (ex: 10 quiz complétés)
    private int xpReward; // XP gagné en débloquant
    private String rarity; // COMMON, RARE, EPIC, LEGENDARY
    private boolean isSecret; // Achievement caché jusqu'au déblocage
    private LocalDateTime createdAt;
    
    // Constructeurs
    public Achievement() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Achievement(String name, String description, String icon, String category, 
                      int requiredValue, int xpReward, String rarity, boolean isSecret) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.category = category;
        this.requiredValue = requiredValue;
        this.xpReward = xpReward;
        this.rarity = rarity;
        this.isSecret = isSecret;
        this.createdAt = LocalDateTime.now();
    }

    // Getters et Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getRequiredValue() {
        return requiredValue;
    }

    public void setRequiredValue(int requiredValue) {
        this.requiredValue = requiredValue;
    }

    public int getXpReward() {
        return xpReward;
    }

    public void setXpReward(int xpReward) {
        this.xpReward = xpReward;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public boolean isSecret() {
        return isSecret;
    }

    public void setSecret(boolean secret) {
        isSecret = secret;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}