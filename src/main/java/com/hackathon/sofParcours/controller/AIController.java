package com.hackathon.sofParcours.controller;

import com.hackathon.sofParcours.controller.dto.AIRequest;
import com.hackathon.sofParcours.controller.dto.AIResponse;
import com.hackathon.sofParcours.service.AIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final AIService aiService;

    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    @Operation(
            summary = "Interroger l'agent IA GPT-5",
            description = "Envoie un prompt à l'agent IA et retourne la réponse textuelle.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = AIRequest.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Réponse IA générée",
                            content = @Content(schema = @Schema(implementation = AIResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Requête invalide (prompt vide)"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erreur lors de l'appel à l'API IA"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<AIResponse> askAI(@RequestBody AIRequest request) {
        if (request == null || request.getPrompt() == null || request.getPrompt().trim().isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new AIResponse("Prompt must not be empty"));
        }

        try {
            String aiText = aiService.askAI(request.getPrompt());
            return ResponseEntity.ok(new AIResponse(aiText));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new AIResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AIResponse("AI service error: " + e.getMessage()));
        }
    }
}