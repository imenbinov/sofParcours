package com.hackathon.sofParcours.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "badges")
public class Badge {
    @Id
    private String id;
    private String name;
    private String description;
    private int pointsRequired;
    private String iconUrl;

    public Badge() {}

    public Badge(String id, String name, String description, int pointsRequired, String iconUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.pointsRequired = pointsRequired;
        this.iconUrl = iconUrl;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getPointsRequired() { return pointsRequired; }
    public void setPointsRequired(int pointsRequired) { this.pointsRequired = pointsRequired; }

    public String getIconUrl() { return iconUrl; }
    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }
}
