package com.hackathon.sofParcours.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.sofParcours.model.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.*;

@Service
public class AIService {

    private static final Logger logger = LoggerFactory.getLogger(AIService.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final String apiUrl;
    private final Duration timeout;

    public AIService(
            @Value("${ai.api.key:}") String apiKey,
            @Value("${ai.api.url:https://api.openai.com/v1/gpt-5/chat/completions}") String apiUrl,
            @Value("${ai.api.timeout-ms:10000}") long timeoutMs
    ) {
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
        this.timeout = Duration.ofMillis(timeoutMs);
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    @Cacheable(value = "aiResponses", key = "#prompt")
    public String askAI(String prompt) {
        if (!StringUtils.hasText(prompt)) {
            throw new IllegalArgumentException("Prompt must not be empty");
        }

        if (!StringUtils.hasText(apiKey)) {
            throw new IllegalStateException("AI API key is not configured (property 'ai.api.key').");
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> body = new HashMap<>();
            body.put("model", "gpt-5");
            body.put("prompt", prompt);
            body.put("max_tokens", 256);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Object text = extractTextFromResponse(response.getBody());
                return text != null ? text.toString() : "";
            } else {
                logger.error("AI API returned non-2xx status: {}", response.getStatusCode());
                throw new IllegalStateException("AI API error: " + response.getStatusCode());
            }
        } catch (RestClientException e) {
            logger.error("Error calling AI API", e);
            throw new IllegalStateException("Failed to call AI API", e);
        }
    }

    @SuppressWarnings("unchecked")
    private Object extractTextFromResponse(Map<String, Object> responseBody) {
        try {
            Object choicesObj = responseBody.get("choices");
            if (choicesObj instanceof Iterable) {
                for (Object choice : (Iterable<?>) choicesObj) {
                    if (choice instanceof Map) {
                        Map<String, Object> choiceMap = (Map<String, Object>) choice;
                        Object message = choiceMap.get("message");
                        if (message instanceof Map) {
                            Map<String, Object> messageMap = (Map<String, Object>) message;
                            Object content = messageMap.get("content");
                            if (content != null) {
                                return content;
                            }
                        }
                        Object text = choiceMap.get("text");
                        if (text != null) {
                            return text;
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * Génère des questions de quiz via l'IA et les retourne au format structuré
     */
    public List<Question> generateQuestions(String topic, String difficulty, int numberOfQuestions, String category) {
        String prompt = String.format(
                "Génère %d questions de quiz sur le sujet '%s' avec la difficulté '%s' et la catégorie '%s'. " +
                        "Retourne un tableau JSON avec ce format exact pour chaque question: " +
                        "[{\"text\":\"Question?\",\"options\":[\"Option1\",\"Option2\",\"Option3\",\"Option4\"]," +
                        "\"correctOptionIndex\":0,\"points\":10,\"timeLimit\":30,\"difficulty\":\"%s\",\"category\":\"%s\"}]",
                numberOfQuestions, topic, difficulty, category, difficulty, category
        );

        try {
            // Mode démo : génération de questions par défaut si l'API n'est pas configurée
            if ("demo-key".equals(apiKey)) {
                return generateDemoQuestions(topic, difficulty, numberOfQuestions, category);
            }

            String aiResponse = askAI(prompt);

            // Extraire le JSON du contenu
            String jsonContent = extractJsonArray(aiResponse);

            // Parser le JSON en liste de Questions
            JsonNode questionsNode = objectMapper.readTree(jsonContent);
            List<Question> questions = new ArrayList<>();

            for (JsonNode node : questionsNode) {
                Question question = new Question();
                question.setText(node.path("text").asText());

                List<String> options = new ArrayList<>();
                node.path("options").forEach(opt -> options.add(opt.asText()));
                question.setOptions(options);

                question.setCorrectOptionIndex(node.path("correctOptionIndex").asInt());
                question.setPoints(node.path("points").asInt(10));
                question.setTimeLimit(node.path("timeLimit").asInt(30));
                question.setDifficulty(node.path("difficulty").asText(difficulty));
                question.setCategory(node.path("category").asText(category));

                questions.add(question);
            }

            return questions;

        } catch (Exception e) {
            // En cas d'erreur, générer des questions par défaut
            return generateDemoQuestions(topic, difficulty, numberOfQuestions, category);
        }
    }

    /**
     * Extrait un tableau JSON d'une réponse qui peut contenir du texte additionnel
     */
    private String extractJsonArray(String text) {
        int startIndex = text.indexOf('[');
        int endIndex = text.lastIndexOf(']');

        if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
            return text.substring(startIndex, endIndex + 1);
        }

        return text;
    }

    /**
     * Génère des questions de démonstration pour le hackathon
     */
    private List<Question> generateDemoQuestions(String topic, String difficulty, int numberOfQuestions, String category) {
        List<Question> questions = new ArrayList<>();

        for (int i = 1; i <= numberOfQuestions; i++) {
            Question question = new Question();
            question.setText(String.format("Question %d sur %s - niveau %s: Quelle est la réponse correcte?", i, topic, difficulty));

            question.setOptions(Arrays.asList(
                    "Réponse A pour " + topic,
                    "Réponse B pour " + topic,
                    "Réponse C pour " + topic,
                    "Réponse D pour " + topic
            ));

            question.setCorrectOptionIndex(0);
            question.setPoints(difficulty.equals("EASY") ? 10 : difficulty.equals("MEDIUM") ? 20 : 30);
            question.setTimeLimit(30);
            question.setDifficulty(difficulty);
            question.setCategory(category);

            questions.add(question);
        }

        return questions;
    }
}