package com.hackathon.sofParcours.controller;

import com.hackathon.sofParcours.dto.GenerateQuestionsRequest;
import com.hackathon.sofParcours.dto.QuestionResponse;
import com.hackathon.sofParcours.dto.SubmitAnswerRequest;
import com.hackathon.sofParcours.model.Answer;
import com.hackathon.sofParcours.model.Question;
import com.hackathon.sofParcours.model.Quiz;
import com.hackathon.sofParcours.service.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quizzes")
@CrossOrigin(origins = "*")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    /**
     * Récupérer tous les quiz d'une room
     */
    @GetMapping("/room/{roomCode}")
    public ResponseEntity<List<Quiz>> getQuizzesByRoom(@PathVariable String roomCode) {
        List<Quiz> quizzes = quizService.getQuizzesByRoomCode(roomCode);
        return ResponseEntity.ok(quizzes);
    }

    /**
     * Récupérer un quiz par son ID
     */
    @GetMapping("/{quizId}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable String quizId) {
        Quiz quiz = quizService.getQuizById(quizId);
        return ResponseEntity.ok(quiz);
    }

    /**
     * Créer un nouveau quiz
     */
    @PostMapping
    public ResponseEntity<Quiz> createQuiz(@RequestBody Map<String, String> request) {
        String title = request.get("title");
        String description = request.getOrDefault("description", "");
        String roomCode = request.get("roomCode");
        String createdBy = request.getOrDefault("createdBy", "anonymous");
        String topic = request.getOrDefault("topic", "Général");
        String difficulty = request.getOrDefault("difficulty", "MEDIUM");
        String category = request.getOrDefault("category", "Quiz");
        
        Quiz quiz = quizService.createQuiz(title, description, roomCode, createdBy, topic, difficulty, category);
        return ResponseEntity.ok(quiz);
    }

    /**
     * Démarrer un quiz
     */
    @PostMapping("/{quizId}/start")
    public ResponseEntity<Quiz> startQuiz(@PathVariable String quizId) {
        Quiz quiz = quizService.startQuiz(quizId);
        return ResponseEntity.ok(quiz);
    }

    /**
     * Récupérer ou générer les questions d'un quiz (WORKFLOW PRINCIPAL)
     * Si les questions existent en BDD, elles sont retournées
     * Sinon, elles sont générées par l'IA, affichées et sauvegardées
     */
    @GetMapping("/{quizId}/questions")
    public ResponseEntity<QuestionResponse> getOrGenerateQuestions(
            @PathVariable String quizId,
            @RequestParam(defaultValue = "5") int numberOfQuestions
    ) {
        List<Question> questions = quizService.getOrGenerateQuestions(quizId, numberOfQuestions);
        
        // Déterminer si les questions viennent de la BDD ou de l'IA
        Quiz quiz = quizService.getQuizById(quizId);
        boolean isFromDatabase = quiz.getQuestionIds() != null && !quiz.getQuestionIds().isEmpty();
        
        QuestionResponse response = new QuestionResponse(
                questions,
                !isFromDatabase,
                isFromDatabase ? "DATABASE" : "AI_GENERATED"
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * Générer des questions via l'IA (endpoint direct)
     */
    @PostMapping("/{quizId}/generate-questions")
    public ResponseEntity<QuestionResponse> generateQuestions(
            @PathVariable String quizId,
            @RequestBody GenerateQuestionsRequest request
    ) {
        List<Question> questions = quizService.getOrGenerateQuestions(quizId, request.getNumberOfQuestions());
        
        QuestionResponse response = new QuestionResponse(
                questions,
                true,
                "AI_GENERATED"
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * Soumettre une réponse à une question
     */
    @PostMapping("/answers")
    public ResponseEntity<Answer> submitAnswer(@RequestBody SubmitAnswerRequest request) {
        Answer answer = quizService.submitAnswer(
                request.getQuizId(),
                request.getQuestionId(),
                request.getUserId(),
                request.getSelectedOptionIndex(),
                request.getResponseTimeMs()
        );
        return ResponseEntity.ok(answer);
    }

    /**
     * Récupérer les résultats d'un quiz
     */
    @GetMapping("/{quizId}/results")
    public ResponseEntity<List<Answer>> getQuizResults(@PathVariable String quizId) {
        List<Answer> results = quizService.getQuizResults(quizId);
        return ResponseEntity.ok(results);
    }

    /**
     * Récupérer les réponses d'un utilisateur pour un quiz
     */
    @GetMapping("/{quizId}/results/{userId}")
    public ResponseEntity<List<Answer>> getUserAnswers(
            @PathVariable String quizId,
            @PathVariable String userId
    ) {
        List<Answer> answers = quizService.getUserAnswers(quizId, userId);
        return ResponseEntity.ok(answers);
    }
}
