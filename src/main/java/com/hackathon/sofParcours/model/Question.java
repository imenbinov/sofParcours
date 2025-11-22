package com.hackathon.sofParcours.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "questions")
public class Question {
    @Id
    private String id;
    private String text;
    private List<String> options = new ArrayList<>();
    private int correctOptionIndex;
    private int points;
    private int timeLimit; // en secondes
    private String difficulty; // EASY, MEDIUM, HARD
    private String category;
    private String quizId;

    public Question() {}

    public Question(String id, String text, List<String> options, int correctOptionIndex, 
                   int points, int timeLimit, String difficulty, String category, String quizId) {
        this.id = id;
        this.text = text;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
        this.points = points;
        this.timeLimit = timeLimit;
        this.difficulty = difficulty;
        this.category = category;
        this.quizId = quizId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }

    public int getCorrectOptionIndex() { return correctOptionIndex; }
    public void setCorrectOptionIndex(int correctOptionIndex) { this.correctOptionIndex = correctOptionIndex; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public int getTimeLimit() { return timeLimit; }
    public void setTimeLimit(int timeLimit) { this.timeLimit = timeLimit; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getQuizId() { return quizId; }
    public void setQuizId(String quizId) { this.quizId = quizId; }
}
