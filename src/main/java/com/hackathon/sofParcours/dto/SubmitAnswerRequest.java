package com.hackathon.sofParcours.dto;

public class SubmitAnswerRequest {
    private String userId;
    private String questionId;
    private String quizId;
    private int selectedOptionIndex;
    private long responseTimeMs;

    public SubmitAnswerRequest() {}

    public SubmitAnswerRequest(String userId, String questionId, String quizId, int selectedOptionIndex, long responseTimeMs) {
        this.userId = userId;
        this.questionId = questionId;
        this.quizId = quizId;
        this.selectedOptionIndex = selectedOptionIndex;
        this.responseTimeMs = responseTimeMs;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getQuestionId() { return questionId; }
    public void setQuestionId(String questionId) { this.questionId = questionId; }

    public String getQuizId() { return quizId; }
    public void setQuizId(String quizId) { this.quizId = quizId; }

    public int getSelectedOptionIndex() { return selectedOptionIndex; }
    public void setSelectedOptionIndex(int selectedOptionIndex) { this.selectedOptionIndex = selectedOptionIndex; }

    public long getResponseTimeMs() { return responseTimeMs; }
    public void setResponseTimeMs(long responseTimeMs) { this.responseTimeMs = responseTimeMs; }
}
