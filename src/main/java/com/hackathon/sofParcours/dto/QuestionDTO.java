package com.hackathon.sofParcours.dto;

import java.util.List;

/**
 * DTO pour les données de Question générées par l'IA
 */
public class QuestionDTO {
    
    private String text;
    private String type;
    private List<String> options;
    private String correctAnswer;
    private String explanation;
    private Integer points;

    public QuestionDTO() {
    }

    public QuestionDTO(String text, String type, List<String> options, String correctAnswer, String explanation, Integer points) {
        this.text = text;
        this.type = type;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.explanation = explanation;
        this.points = points;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
