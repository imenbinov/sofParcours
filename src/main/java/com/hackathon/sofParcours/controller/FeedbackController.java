package com.hackathon.sofParcours.controller;

import com.hackathon.sofParcours.model.Comment;
import com.hackathon.sofParcours.repository.CommentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/feedback")
@Tag(name = "Feedback", description = "Commentaires et feedback sur les questions")
public class FeedbackController {

    private final CommentRepository commentRepository;

    public FeedbackController(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Operation(summary = "Ajouter un commentaire sur une question")
    @PostMapping("/questions/{questionId}/comments")
    public ResponseEntity<Comment> addComment(
            @PathVariable String questionId,
            @RequestBody Map<String, String> request
    ) {
        try {
            String userId = request.get("userId");
            String username = request.get("username");
            String content = request.get("content");

            Comment comment = new Comment();
            comment.setQuestionId(questionId);
            comment.setUserId(userId);
            comment.setUsername(username);
            comment.setContent(content);
            comment.setCreatedAt(LocalDateTime.now());

            Comment saved = commentRepository.save(comment);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Obtenir les commentaires d'une question")
    @GetMapping("/questions/{questionId}/comments")
    public ResponseEntity<List<Comment>> getComments(@PathVariable String questionId) {
        List<Comment> comments = commentRepository.findByQuestionId(questionId);
        return ResponseEntity.ok(comments);
    }
}
