package com.hackathon.sofParcours.dto;

/**
 * DTO pour les données de Quiz générées par l'IA
 */
public class QuizDTO {
    
    private String title;
    private String description;
    private Integer duration;

    public QuizDTO() {
    }

    public QuizDTO(String title, String description, Integer duration) {
        this.title = title;
        this.description = description;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
