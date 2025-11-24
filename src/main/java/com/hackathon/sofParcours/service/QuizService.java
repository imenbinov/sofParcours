package com.hackathon.sofParcours.service;

import com.hackathon.sofParcours.model.Quiz;
import com.hackathon.sofParcours.model.Question;
import com.hackathon.sofParcours.repository.QuizRepository;
import com.hackathon.sofParcours.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service de consultation des Quiz
 * IMPORTANT: La création de Quiz se fait UNIQUEMENT via RoomCreationService avec l'IA
 */
@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final ScoringService scoringService;

    public QuizService(
            QuizRepository quizRepository,
            QuestionRepository questionRepository,
            ScoringService scoringService
    ) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.scoringService = scoringService;
    }

    /**
     * Récupère un quiz par ID
     */
    public Optional<Quiz> getQuizById(String id) {
        return quizRepository.findById(id);
    }

    /**
     * Liste les quiz d'une room
     */
    public List<Quiz> getQuizzesByRoomCode(String roomCode) {
        // Si le repository retourne Optional<Quiz>, convertir en List
        return quizRepository.findByRoomCode(roomCode)
                .map(List::of)
                .orElse(List.of());
    }

    /**
     * Récupère les questions d'un quiz
     */
    public List<Question> getQuestionsByQuizId(String quizId) {
        return questionRepository.findByQuizId(quizId);
    }

    /**
     * Résultats d'un quiz (leaderboard)
     */
    public List<Map<String, Object>> getQuizResults(String quizId) {
        return scoringService.getLeaderboard(quizId);
    }

    /**
     * Résultats d'un utilisateur pour un quiz
     */
    public Map<String, Object> getUserQuizResults(String quizId, String userId) {
        return scoringService.calculateUserScore(quizId, userId);
    }
}
