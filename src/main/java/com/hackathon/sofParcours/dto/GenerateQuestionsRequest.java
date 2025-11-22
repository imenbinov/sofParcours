package com.hackathon.sofParcours.dto;

public class GenerateQuestionsRequest {
    private String quizId;
    private String topic;
    private String difficulty; // EASY, MEDIUM, HARD
    private int numberOfQuestions;
    private String category;

    public GenerateQuestionsRequest() {}

    public GenerateQuestionsRequest(String quizId, String topic, String difficulty, int numberOfQuestions, String category) {
        this.quizId = quizId;
        this.topic = topic;
        this.difficulty = difficulty;
        this.numberOfQuestions = numberOfQuestions;
        this.category = category;
    }

    public String getQuizId() { return quizId; }
    public void setQuizId(String quizId) { this.quizId = quizId; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public int getNumberOfQuestions() { return numberOfQuestions; }
    public void setNumberOfQuestions(int numberOfQuestions) { this.numberOfQuestions = numberOfQuestions; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
