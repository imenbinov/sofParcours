package com.hackathon.sofParcours.service;

import com.hackathon.sofParcours.model.Room;
import com.hackathon.sofParcours.model.Quiz;
import com.hackathon.sofParcours.model.Question;
import com.hackathon.sofParcours.repository.RoomRepository;
import com.hackathon.sofParcours.repository.QuizRepository;
import com.hackathon.sofParcours.repository.QuestionRepository;
import com.hackathon.sofParcours.dto.RoomDTO;
import com.hackathon.sofParcours.dto.QuizDTO;
import com.hackathon.sofParcours.dto.QuestionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class RoomCreationService {

    private static final Logger logger = LoggerFactory.getLogger(RoomCreationService.class);

    private final RoomRepository roomRepository;
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final AIService aiService;

    public RoomCreationService(
            RoomRepository roomRepository,
            QuizRepository quizRepository,
            QuestionRepository questionRepository,
            AIService aiService) {
        this.roomRepository = roomRepository;
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.aiService = aiService;
    }

    /**
     * Recherche ou crée une Room basée sur une requête utilisateur
     * Endpoint idempotent avec gestion des doublons
     */
    @Transactional
    public Room findOrCreateByQuery(String query, String userProfile) {
        logger.info("Search or create room for query: '{}', userProfile: '{}'", query, userProfile);

        // 1. Normaliser la requête en slug
        String slug = normalizeToSlug(query);
        logger.debug("Normalized slug: '{}'", slug);

        // 2. Rechercher si une Room existe déjà
        Optional<Room> existingRoom = roomRepository.findBySlug(slug);
        if (existingRoom.isPresent()) {
            logger.info("Room found with slug '{}', returning existing room", slug);
            return existingRoom.get();
        }

        // 3. La Room n'existe pas, générer via IA
        logger.info("Room not found, generating with AI for slug '{}'", slug);
        
        try {
            return createRoomWithAI(slug, query, userProfile);
        } catch (DuplicateKeyException e) {
            // Gestion de l'idempotence : un autre thread a créé la Room en parallèle
            logger.warn("Duplicate key detected, reloading room with slug '{}'", slug);
            return roomRepository.findBySlug(slug)
                    .orElseThrow(() -> new RuntimeException("Room was created but could not be retrieved"));
        } catch (Exception e) {
            logger.error("AI generation failed for query '{}'", query, e);
            throw new RuntimeException("AI generation failed: " + e.getMessage(), e);
        }
    }

    /**
     * Crée une Room complète avec Quiz et Questions via l'IA
     */
    private Room createRoomWithAI(String slug, String rawQuery, String userProfile) {
        try {
            // Appel au service IA
            logger.debug("Calling AI service for room generation");
            var aiResponse = aiService.generateRoomWithQuiz(slug, rawQuery, userProfile);

            // Extraction des DTOs
            RoomDTO roomDTO = aiResponse.getRoomDTO();
            QuizDTO quizDTO = aiResponse.getQuizDTO();
            List<QuestionDTO> questionDTOs = aiResponse.getQuestionDTOs();

            // Conversion DTO → Model
            Room room = convertRoomDTOToModel(roomDTO, slug);
            Quiz quiz = convertQuizDTOToModel(quizDTO, room.getId());
            List<Question> questions = convertQuestionDTOsToModels(questionDTOs, quiz.getId());

            // Ajout des métadonnées IA
            room.setGeneratedByAI(true);
            room.setGeneratedAt(LocalDateTime.now());
            room.setSlug(slug);

            // Sauvegarde en MongoDB
            Room savedRoom = roomRepository.save(room);
            logger.info("Room saved with ID: {}", savedRoom.getId());

            quiz.setRoomId(savedRoom.getId());
            Quiz savedQuiz = quizRepository.save(quiz);
            logger.info("Quiz saved with ID: {}", savedQuiz.getId());

            for (Question question : questions) {
                question.setQuizId(savedQuiz.getId());
            }
            List<Question> savedQuestions = questionRepository.saveAll(questions);
            logger.info("Saved {} questions", savedQuestions.size());

            // Associer Quiz et Questions à la Room
            savedRoom.setQuiz(savedQuiz);
            savedQuiz.setQuestions(savedQuestions);

            return savedRoom;

        } catch (Exception e) {
            logger.error("Error during AI room creation", e);
            throw new RuntimeException("AI generation failed: " + e.getMessage(), e);
        }
    }

    /**
     * Normalise un texte en slug
     * Ex: "DevOps avancé" → "devops-avance"
     */
    private String normalizeToSlug(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty");
        }

        // Normalisation Unicode (décomposition des accents)
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        
        // Suppression des diacritiques
        normalized = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        
        // Conversion en minuscules
        normalized = normalized.toLowerCase();
        
        // Remplacement des espaces et caractères spéciaux par des tirets
        normalized = normalized.replaceAll("[^a-z0-9]+", "-");
        
        // Suppression des tirets en début et fin
        normalized = normalized.replaceAll("^-+|-+$", "");
        
        return normalized;
    }

    /**
     * Convertit RoomDTO en Room
     */
    private Room convertRoomDTOToModel(RoomDTO dto, String slug) {
        Room room = new Room();
        room.setName(dto.getName());
        room.setDescription(dto.getDescription());
        room.setSlug(slug);
        room.setCode(generateRoomCode());
        room.setCreatedBy(dto.getCreatedBy() != null ? dto.getCreatedBy() : "AI");
        room.setStatus("active");
        room.setCreatedAt(LocalDateTime.now());
        room.setParticipants(new ArrayList<>());
        return room;
    }

    /**
     * Convertit QuizDTO en Quiz
     */
    private Quiz convertQuizDTOToModel(QuizDTO dto, String roomId) {
        Quiz quiz = new Quiz();
        quiz.setTitle(dto.getTitle());
        quiz.setDescription(dto.getDescription());
        quiz.setRoomId(roomId);
        quiz.setDuration(dto.getDuration() != null ? dto.getDuration() : 30);
        quiz.setCreatedAt(LocalDateTime.now());
        return quiz;
    }

    /**
     * Convertit une liste de QuestionDTO en Questions
     */
    private List<Question> convertQuestionDTOsToModels(List<QuestionDTO> dtos, String quizId) {
        List<Question> questions = new ArrayList<>();
        
        for (int i = 0; i < dtos.size(); i++) {
            QuestionDTO dto = dtos.get(i);
            Question question = new Question();
            question.setQuizId(quizId);
            question.setText(dto.getText());
            question.setType(dto.getType() != null ? dto.getType() : "multiple_choice");
            question.setOptions(dto.getOptions() != null ? dto.getOptions() : new ArrayList<>());
            question.setCorrectAnswer(dto.getCorrectAnswer());
            question.setExplanation(dto.getExplanation());
            question.setPoints(dto.getPoints() != null ? dto.getPoints() : 10);
            question.setOrder(i + 1);
            questions.add(question);
        }
        
        return questions;
    }

    /**
     * Génère un code unique de room (6 caractères alphanumériques)
     */
    private String generateRoomCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        
        for (int i = 0; i < 6; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }
        
        return code.toString();
    }
}
