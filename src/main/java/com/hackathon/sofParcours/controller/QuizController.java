package com.hackathon.sofParcours.controller;

import com.hackathon.sofParcours.model.Answer;
import com.hackathon.sofParcours.model.Question;
import com.hackathon.sofParcours.model.Quiz;
import com.hackathon.sofParcours.service.QuizService;
import com.hackathon.sofParcours.service.ScoringService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quiz")
@Tag(name = "Quiz", description = "Gestion des quiz et questions")
public class QuizController {

    private final QuizService quizService;
    private final ScoringService scoringService;

    public QuizController(QuizService quizService, ScoringService scoringService) {
        this.quizService = quizService;
        this.scoringService = scoringService;
    }

    @Operation(summary = "Créer un nouveau quiz")
    @PostMapping
    public ResponseEntity<Quiz> createQuiz(@RequestBody Map<String, Object> request) {
        try {
            String title = (String) request.get("title");
            String description = (String) request.get("description");
            String roomCode = (String) request.get("roomCode");
            String createdBy = (String) request.get("createdBy");
            @SuppressWarnings("unchecked")
            List<String> questionIds = (List<String>) request.get("questionIds");

            Quiz quiz = quizService.createQuiz(title, description, roomCode, createdBy, questionIds);
            return ResponseEntity.ok(quiz);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Démarrer un quiz")
    @PostMapping("/{quizId}/start")
    public ResponseEntity<Quiz> startQuiz(@PathVariable String quizId) {
        try {
            Quiz quiz = quizService.startQuiz(quizId);
            return ResponseEntity.ok(quiz);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Soumettre une réponse")
    @PostMapping("/{quizId}/answer")
    public ResponseEntity<Answer> submitAnswer(
            @PathVariable String quizId,
            @RequestBody Map<String, Object> request
    ) {
        try {
            String questionId = (String) request.get("questionId");
            String userId = (String) request.get("userId");
            int selectedOptionIndex = (int) request.get("selectedOptionIndex");
            long responseTimeMs = ((Number) request.get("responseTimeMs")).longValue();

            Answer answer = quizService.submitAnswer(quizId, questionId, userId, selectedOptionIndex, responseTimeMs);
            return ResponseEntity.ok(answer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Obtenir le classement d'un quiz")
    @GetMapping("/{quizId}/leaderboard")
    public ResponseEntity<List<Map<String, Object>>> getLeaderboard(@PathVariable String quizId) {
        try {
            List<Map<String, Object>> leaderboard = scoringService.getLeaderboard(quizId);
            return ResponseEntity.ok(leaderboard);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtenir un quiz par ID")
    @GetMapping("/{quizId}")
    public ResponseEntity<Quiz> getQuiz(@PathVariable String quizId) {
        try {
            Quiz quiz = quizService.getQuizById(quizId);
            return ResponseEntity.ok(quiz);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Créer une question")
    @PostMapping("/questions")
    public ResponseEntity<Question> createQuestion(@RequestBody Question question) {
        try {
            Question created = quizService.createQuestion(question);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Lister toutes les questions")
    @GetMapping("/questions")
    public ResponseEntity<List<Question>> getAllQuestions() {
        List<Question> questions = quizService.getAllQuestions();
        return ResponseEntity.ok(questions);
    }
}
