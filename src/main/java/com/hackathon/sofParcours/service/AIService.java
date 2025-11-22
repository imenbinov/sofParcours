package com.hackathon.sofParcours.service;

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
import java.util.HashMap;
import java.util.Map;

@Service
public class AIService {

    private static final Logger logger = LoggerFactory.getLogger(AIService.class);

    private final RestTemplate restTemplate;
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
}