package com.hackathon.sofParcours.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.sofParcours.dto.AIRoomResponse;
import com.hackathon.sofParcours.dto.RoomDTO;
import com.hackathon.sofParcours.dto.QuizDTO;
import com.hackathon.sofParcours.dto.QuestionDTO;
import com.hackathon.sofParcours.model.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AIService {

    private static final Logger logger = LoggerFactory.getLogger(AIService.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final String apiUrl;

    public AIService(
            @Value("${ai.api.key:}") String apiKey,
            @Value("${ai.api.url:https://api.openai.com/v1/chat/completions}") String apiUrl,
            @Value("${ai.api.timeout-ms:10000}") long timeoutMs
    ) {
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Génère une Room complète avec Quiz et Questions via l'IA
     */
    public AIRoomResponse generateRoomWithQuiz(String slug, String rawQuery, String userProfile) {
        logger.info("Generating room with AI for slug: '{}', query: '{}'", slug, rawQuery);

        try {
            // Construction du prompt pour l'IA
            String prompt = buildRoomQuizPrompt(slug, rawQuery, userProfile);

            // Appel à l'API OpenAI
            String aiResponse = callOpenAIForRoomGeneration(prompt);

            // Parse la réponse JSON de l'IA
            AIRoomResponse roomResponse = parseRoomGenerationResponse(aiResponse, slug, rawQuery);

            logger.info("Successfully generated room with {} questions",
                    roomResponse.getQuestionDTOs().size());

            return roomResponse;

        } catch (Exception e) {
            logger.error("Failed to generate room with AI", e);
            throw new RuntimeException("AI generation failed: " + e.getMessage(), e);
        }
    }

    /**
     * Construit le prompt pour la génération de Room + Quiz
     */
    private String buildRoomQuizPrompt(String slug, String rawQuery, String userProfile) {
        return String.format("""
            Tu es un expert en création de quiz éducatifs.
            
            Crée un quiz complet sur le sujet suivant : "%s"
            Profil utilisateur : %s
            
            Génère un objet JSON avec cette structure exacte :
            {
              "room": {
                "name": "Nom de la room",
                "description": "Description détaillée",
                "createdBy": "AI"
              },
              "quiz": {
                "title": "Titre du quiz",
                "description": "Description du quiz",
                "duration": 30
              },
              "questions": [
                {
                  "text": "Question ?",
                  "type": "multiple_choice",
                  "options": ["Option A", "Option B", "Option C", "Option D"],
                  "correctAnswer": "Option A",
                  "explanation": "Explication de la réponse",
                  "points": 10
                }
              ]
            }
            
            Génère exactement 10 questions de difficulté progressive.
            Retourne UNIQUEMENT le JSON, sans texte avant ou après.
            """, rawQuery, userProfile);
    }

    /**
     * Appelle OpenAI pour la génération de Room
     */
    private String callOpenAIForRoomGeneration(String prompt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-3.5-turbo");
            requestBody.put("messages", List.of(
                Map.of("role", "system", "content", "You are a helpful quiz generator."),
                Map.of("role", "user", "content", prompt)
            ));
            requestBody.put("temperature", 0.7);
            requestBody.put("max_tokens", 2000);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                request,
                String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode root = objectMapper.readTree(response.getBody());
                String content = root.path("choices").get(0).path("message").path("content").asText();
                return content.trim();
            } else {
                throw new RuntimeException("OpenAI API returned status: " + response.getStatusCode());
            }

        } catch (Exception e) {
            logger.error("Error calling OpenAI API for room generation", e);
            throw new RuntimeException("Failed to call AI service: " + e.getMessage(), e);
        }
    }

    /**
     * Parse la réponse JSON de l'IA pour Room + Quiz
     */
    private AIRoomResponse parseRoomGenerationResponse(String jsonResponse, String slug, String rawQuery) {
        try {
            // Nettoyer le JSON (supprimer les backticks markdown si présents)
            jsonResponse = jsonResponse.replaceAll("```json\\s*", "").replaceAll("```\\s*$", "").trim();

            JsonNode root = objectMapper.readTree(jsonResponse);

            // Parser Room
            JsonNode roomNode = root.path("room");
            RoomDTO roomDTO = new RoomDTO(
                roomNode.path("name").asText(rawQuery),
                roomNode.path("description").asText("Quiz généré par IA sur " + rawQuery),
                roomNode.path("createdBy").asText("AI")
            );

            // Parser Quiz
            JsonNode quizNode = root.path("quiz");
            QuizDTO quizDTO = new QuizDTO(
                quizNode.path("title").asText(rawQuery + " - Quiz"),
                quizNode.path("description").asText("Quiz interactif"),
                quizNode.path("duration").asInt(30)
            );

            // Parser Questions
            List<QuestionDTO> questionDTOs = new ArrayList<>();
            JsonNode questionsNode = root.path("questions");
            
            if (questionsNode.isArray()) {
                for (JsonNode questionNode : questionsNode) {
                    List<String> options = new ArrayList<>();
                    JsonNode optionsNode = questionNode.path("options");
                    if (optionsNode.isArray()) {
                        optionsNode.forEach(opt -> options.add(opt.asText()));
                    }

                    QuestionDTO questionDTO = new QuestionDTO(
                        questionNode.path("text").asText(),
                        questionNode.path("type").asText("multiple_choice"),
                        options,
                        questionNode.path("correctAnswer").asText(),
                        questionNode.path("explanation").asText(),
                        questionNode.path("points").asInt(10)
                    );
                    questionDTOs.add(questionDTO);
                }
            }

            return new AIRoomResponse(roomDTO, quizDTO, questionDTOs);

        } catch (Exception e) {
            logger.error("Failed to parse AI response for room generation", e);
            throw new RuntimeException("Failed to parse AI response: " + e.getMessage(), e);
        }
    }

    /**
     * Génère des questions (méthode de compatibilité avec l'ancien code)
     * @deprecated Utiliser generateRoomWithQuiz à la place
     */
    @Deprecated
    public List<Question> generateQuestions(String topic, String difficulty, int numberOfQuestions, String category) {
        logger.warn("Using deprecated generateQuestions method. Consider using generateRoomWithQuiz instead.");
        
        // Générer des questions de démo simples pour compatibilité
        List<Question> questions = new ArrayList<>();
        
        for (int i = 1; i <= numberOfQuestions; i++) {
            Question question = new Question();
            question.setText("Question " + i + " sur " + topic + " (" + difficulty + ")");
            question.setType("multiple_choice");
            question.setOptions(List.of("Option A", "Option B", "Option C", "Option D"));
            question.setCorrectAnswer("Option A");
            question.setExplanation("Explication pour la question " + i);
            question.setPoints(10);
            question.setOrder(i);
            questions.add(question);
        }
        
        return questions;
    }
}