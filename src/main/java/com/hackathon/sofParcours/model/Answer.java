package com.hackathon.sofParcours.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "answers")
public class Answer {
    @Id
    private String id;
    private String userId;
    private String questionId;
    private String quizId;
    private int selectedOptionIndex;
    private boolean isCorrect;
    private int pointsEarned;
    private long responseTimeMs;
    private LocalDateTime answeredAt;

    public Answer() {}

    public Answer(String id, String userId, String questionId, String quizId, 
                 int selectedOptionIndex, boolean isCorrect, int pointsEarned, 
                 long responseTimeMs, LocalDateTime answeredAt) {
        this.id = id;
        this.userId = userId;
        this.questionId = questionId;
        this.quizId = quizId;
        this.selectedOptionIndex = selectedOptionIndex;
        this.isCorrect = isCorrect;
        this.pointsEarned = pointsEarned;
        this.responseTimeMs = responseTimeMs;
        this.answeredAt = answeredAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getQuestionId() { return questionId; }
    public void setQuestionId(String questionId) { this.questionId = questionId; }

    public String getQuizId() { return quizId; }
    public void setQuizId(String quizId) { this.quizId = quizId; }

    public int getSelectedOptionIndex() { return selectedOptionIndex; }
    public void setSelectedOptionIndex(int selectedOptionIndex) { this.selectedOptionIndex = selectedOptionIndex; }

    public boolean isCorrect() { return isCorrect; }
    public void setCorrect(boolean correct) { isCorrect = correct; }

    public int getPointsEarned() { return pointsEarned; }
    public void setPointsEarned(int pointsEarned) { this.pointsEarned = pointsEarned; }

    public long getResponseTimeMs() { return responseTimeMs; }
    public void setResponseTimeMs(long responseTimeMs) { this.responseTimeMs = responseTimeMs; }

    public LocalDateTime getAnsweredAt() { return answeredAt; }
    public void setAnsweredAt(LocalDateTime answeredAt) { this.answeredAt = answeredAt; }
}
