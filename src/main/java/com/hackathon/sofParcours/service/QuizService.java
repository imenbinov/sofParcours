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
    private final AIService aiService;

    public QuizService(
            QuizRepository quizRepository,
            QuestionRepository questionRepository,
            AnswerRepository answerRepository,
            RoomRepository roomRepository,
            AIService aiService
    ) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.roomRepository = roomRepository;
        this.aiService = aiService;
    }

    public Quiz createQuiz(String title, String description, String roomCode, String createdBy,
                          String topic, String difficulty, String category) {
        Room room = roomRepository.findByCode(roomCode)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setDescription(description);
        quiz.setRoomCode(roomCode);
        quiz.setStatus("PENDING");
        quiz.setCurrentQuestionIndex(0);
        quiz.setCreatedBy(createdBy);
        quiz.setCreatedAt(LocalDateTime.now());
        quiz.setTopic(topic);
        quiz.setDifficulty(difficulty);
        quiz.setCategory(category);

        Quiz savedQuiz = quizRepository.save(quiz);

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
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Question not found"));

        boolean isCorrect = question.getCorrectOptionIndex() == selectedOptionIndex;
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

    public List<Quiz> getQuizzesByRoomCode(String roomCode) {
        return quizRepository.findAllByRoomCode(roomCode);
    }

    private int calculatePoints(int basePoints, long responseTimeMs, int timeLimitSeconds) {
        long timeLimitMs = timeLimitSeconds * 1000L;
        double timeRatio = 1.0 - ((double) responseTimeMs / timeLimitMs);
        int bonus = (int) (basePoints * 0.5 * Math.max(0, timeRatio));
        return basePoints + bonus;
    }

    /**
     * Récupère ou génère les questions d'un quiz
     * Si les questions existent en BDD, elles sont retournées
     * Sinon, elles sont générées par l'IA et sauvegardées
     */
    public List<Question> getOrGenerateQuestions(String quizId, int numberOfQuestions) {
        Quiz quiz = getQuizById(quizId);

        // Si le quiz a déjà des questions, les récupérer
        if (quiz.getQuestionIds() != null && !quiz.getQuestionIds().isEmpty()) {
            return questionRepository.findAllById(quiz.getQuestionIds());
        }

        // Sinon, générer via l'IA
        String topic = quiz.getTopic() != null ? quiz.getTopic() : "Général";
        String difficulty = quiz.getDifficulty() != null ? quiz.getDifficulty() : "MEDIUM";
        String category = quiz.getCategory() != null ? quiz.getCategory() : "Quiz";

        List<Question> generatedQuestions = aiService.generateQuestions(topic, difficulty, numberOfQuestions, category);

        // Sauvegarder les questions en BDD
        List<Question> savedQuestions = new ArrayList<>();
        List<String> questionIds = new ArrayList<>();

        for (Question question : generatedQuestions) {
            question.setQuizId(quizId);
            Question saved = questionRepository.save(question);
            savedQuestions.add(saved);
            questionIds.add(saved.getId());
        }

        // Mettre à jour le quiz avec les IDs des questions
        quiz.setQuestionIds(questionIds);
        quizRepository.save(quiz);

        return savedQuestions;
    }

    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }

    public List<Answer> getQuizResults(String quizId) {
        return answerRepository.findByQuizId(quizId);
    }

    public List<Answer> getUserAnswers(String quizId, String userId) {
        return answerRepository.findByQuizIdAndUserId(quizId, userId);
    }
}
