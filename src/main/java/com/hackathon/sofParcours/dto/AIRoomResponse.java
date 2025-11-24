package com.hackathon.sofParcours.dto;

import java.util.List;

/**
 * Réponse complète de l'IA contenant Room, Quiz et Questions
 */
public class AIRoomResponse {
    
    private RoomDTO roomDTO;
    private QuizDTO quizDTO;
    private List<QuestionDTO> questionDTOs;

    public AIRoomResponse() {
    }

    public AIRoomResponse(RoomDTO roomDTO, QuizDTO quizDTO, List<QuestionDTO> questionDTOs) {
        this.roomDTO = roomDTO;
        this.quizDTO = quizDTO;
        this.questionDTOs = questionDTOs;
    }

    public RoomDTO getRoomDTO() {
        return roomDTO;
    }

    public void setRoomDTO(RoomDTO roomDTO) {
        this.roomDTO = roomDTO;
    }

    public QuizDTO getQuizDTO() {
        return quizDTO;
    }

    public void setQuizDTO(QuizDTO quizDTO) {
        this.quizDTO = quizDTO;
    }

    public List<QuestionDTO> getQuestionDTOs() {
        return questionDTOs;
    }

    public void setQuestionDTOs(List<QuestionDTO> questionDTOs) {
        this.questionDTOs = questionDTOs;
    }
}
