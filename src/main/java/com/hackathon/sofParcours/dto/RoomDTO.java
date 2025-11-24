package com.hackathon.sofParcours.dto;

/**
 * DTO pour les données de Room générées par l'IA
 */
public class RoomDTO {
    
    private String name;
    private String description;
    private String createdBy;

    public RoomDTO() {
    }

    public RoomDTO(String name, String description, String createdBy) {
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
