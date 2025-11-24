package com.hackathon.sofParcours.controller;

import com.hackathon.sofParcours.model.Quiz;
import com.hackathon.sofParcours.model.Question;
import com.hackathon.sofParcours.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller pour la consultation des Quiz
 * IMPORTANT: La création de Quiz se fait UNIQUEMENT via /api/rooms/search-or-create avec l'IA
 */
@RestController
@RequestMapping("/api/quiz")
@Tag(name = "📝 Quiz", description = "Consultation des quiz (Création via IA uniquement)")
@CrossOrigin(origins = "*")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    /**
     * Consultation uniquement - Récupérer un quiz par ID
     */
    @Operation(summary = "🔍 Récupérer un quiz par ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getQuiz(
            @Parameter(description = "ID du quiz")
            @PathVariable String id
    ) {
        return quizService.getQuizById(id)
                .map(quiz -> ResponseEntity.ok(quiz))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Consultation uniquement - Lister les quiz d'une room
     */
    @Operation(summary = "📋 Lister les quiz d'une room")
    @GetMapping("/room/{roomCode}")
    public ResponseEntity<List<Quiz>> getQuizzesByRoom(
            @Parameter(description = "Code de la room", example = "ABC123")
            @PathVariable String roomCode
    ) {
        List<Quiz> quizzes = quizService.getQuizzesByRoomCode(roomCode);
        return ResponseEntity.ok(quizzes);
    }

    /**
     * Consultation uniquement - Récupérer les questions d'un quiz
     */
    @Operation(summary = "❓ Récupérer les questions d'un quiz")
    @GetMapping("/{quizId}/questions")
    public ResponseEntity<List<Question>> getQuestions(
            @Parameter(description = "ID du quiz")
            @PathVariable String quizId
    ) {
        List<Question> questions = quizService.getQuestionsByQuizId(quizId);
        return ResponseEntity.ok(questions);
    }

    /**
     * Consultation uniquement - Résultats d'un quiz
     */
    @Operation(summary = "📊 Résultats d'un quiz")
    @GetMapping("/{quizId}/results")
    public ResponseEntity<?> getResults(
            @Parameter(description = "ID du quiz")
            @PathVariable String quizId
    ) {
        var results = quizService.getQuizResults(quizId);
        return ResponseEntity.ok(results);
    }

    /**
     * Consultation uniquement - Résultats d'un utilisateur
     */
    @Operation(summary = "👤 Résultats d'un utilisateur pour un quiz")
    @GetMapping("/{quizId}/results/{userId}")
    public ResponseEntity<?> getUserResults(
            @Parameter(description = "ID du quiz")
            @PathVariable String quizId,
            @Parameter(description = "ID de l'utilisateur")
            @PathVariable String userId
    ) {
        var results = quizService.getUserQuizResults(quizId, userId);
        return ResponseEntity.ok(results);
    }
}
