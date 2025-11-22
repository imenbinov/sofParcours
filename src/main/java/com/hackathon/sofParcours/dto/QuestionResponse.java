package com.hackathon.sofParcours.dto;

import com.hackathon.sofParcours.model.Question;

import java.util.List;

public class QuestionResponse {
    private List<Question> questions;
    private boolean generatedByAI;
    private String source; // "DATABASE" ou "AI_GENERATED"

    public QuestionResponse() {}

    public QuestionResponse(List<Question> questions, boolean generatedByAI, String source) {
        this.questions = questions;
        this.generatedByAI = generatedByAI;
        this.source = source;
    }

    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }

    public boolean isGeneratedByAI() { return generatedByAI; }
    public void setGeneratedByAI(boolean generatedByAI) { this.generatedByAI = generatedByAI; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
}
