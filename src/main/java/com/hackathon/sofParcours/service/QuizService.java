package com.hackathon.sofParcours.service;

import com.hackathon.sofParcours.model.*;
import com.hackathon.sofParcours.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final RoomRepository roomRepository;

    public QuizService(
            QuizRepository quizRepository,
            QuestionRepository questionRepository,
            AnswerRepository answerRepository,
            RoomRepository roomRepository
    ) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.roomRepository = roomRepository;
    }

    public Quiz createQuiz(String title, String description, String roomCode, String createdBy, List<String> questionIds) {
        Room room = roomRepository.findByCode(roomCode)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setDescription(description);
        quiz.setRoomCode(roomCode);
        quiz.setQuestionIds(questionIds);
        quiz.setStatus("PENDING");
        quiz.setCurrentQuestionIndex(0);
        quiz.setCreatedBy(createdBy);

        Quiz savedQuiz = quizRepository.save(quiz);

        // Mise à jour de la room avec l'ID du quiz
        room.setCurrentQuizId(savedQuiz.getId());
        roomRepository.save(room);

        return savedQuiz;
    }

    public Quiz startQuiz(String quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found"));

        quiz.setStatus("IN_PROGRESS");
        quiz.setStartedAt(LocalDateTime.now());

        return quizRepository.save(quiz);
    }

    public Answer submitAnswer(String quizId, String questionId, String userId, int selectedOptionIndex, long responseTimeMs) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found"));

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Question not found"));

        boolean isCorrect = selectedOptionIndex == question.getCorrectOptionIndex();
        int pointsEarned = isCorrect ? calculatePoints(question.getPoints(), responseTimeMs, question.getTimeLimit()) : 0;

        Answer answer = new Answer();
        answer.setUserId(userId);
        answer.setQuestionId(questionId);
        answer.setQuizId(quizId);
        answer.setSelectedOptionIndex(selectedOptionIndex);
        answer.setCorrect(isCorrect);
        answer.setPointsEarned(pointsEarned);
        answer.setResponseTimeMs(responseTimeMs);
        answer.setAnsweredAt(LocalDateTime.now());

        return answerRepository.save(answer);
    }

    public Quiz getQuizById(String quizId) {
        return quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found"));
    }

    public Quiz getQuizByRoomCode(String roomCode) {
        return quizRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new IllegalArgumentException("No quiz found for this room"));
    }

    private int calculatePoints(int basePoints, long responseTimeMs, int timeLimitSeconds) {
        // Bonus de rapidité : plus vite = plus de points
        long timeLimitMs = timeLimitSeconds * 1000L;
        double timeRatio = 1.0 - ((double) responseTimeMs / timeLimitMs);
        int bonus = (int) (basePoints * 0.5 * Math.max(0, timeRatio)); // Max 50% bonus
        return basePoints + bonus;
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }
}
