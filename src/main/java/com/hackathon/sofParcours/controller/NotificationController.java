package com.hackathon.sofParcours.controller;

import com.hackathon.sofParcours.dto.NotificationDTO;
import com.hackathon.sofParcours.model.Notification;
import com.hackathon.sofParcours.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Contrôleur pour les notifications
 */
@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notifications", description = "API de gestion des notifications")
public class NotificationController {
    
    @Autowired
    private NotificationService notificationService;
    
    /**
     * Récupère toutes les notifications d'un utilisateur
     */
    @GetMapping("/{userId}")
    @Operation(summary = "Liste des notifications", 
               description = "Récupère toutes les notifications d'un utilisateur")
    public ResponseEntity<List<NotificationDTO>> getUserNotifications(@PathVariable String userId) {
        List<Notification> notifications = notificationService.getUserNotifications(userId);
        
        List<NotificationDTO> dtos = notifications.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(dtos);
    }
    
    /**
     * Récupère les notifications non lues
     */
    @GetMapping("/{userId}/unread")
    @Operation(summary = "Notifications non lues", 
               description = "Récupère uniquement les notifications non lues")
    public ResponseEntity<List<NotificationDTO>> getUnreadNotifications(@PathVariable String userId) {
        List<Notification> notifications = notificationService.getUnreadNotifications(userId);
        
        List<NotificationDTO> dtos = notifications.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(dtos);
    }
    
    /**
     * Compte les notifications non lues
     */
    @GetMapping("/{userId}/unread/count")
    @Operation(summary = "Nombre de notifications non lues", 
               description = "Retourne le nombre de notifications non lues")
    public ResponseEntity<Map<String, Long>> getUnreadCount(@PathVariable String userId) {
        long count = notificationService.countUnreadNotifications(userId);
        
        Map<String, Long> response = new HashMap<>();
        response.put("unreadCount", count);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Marque une notification comme lue
     */
    @PutMapping("/{notificationId}/read")
    @Operation(summary = "Marquer comme lue", 
               description = "Marque une notification comme lue")
    public ResponseEntity<Map<String, String>> markAsRead(@PathVariable String notificationId) {
        notificationService.markAsRead(notificationId);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Notification marquée comme lue");
        response.put("notificationId", notificationId);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Marque toutes les notifications comme lues
     */
    @PutMapping("/{userId}/read-all")
    @Operation(summary = "Tout marquer comme lu", 
               description = "Marque toutes les notifications comme lues")
    public ResponseEntity<Map<String, String>> markAllAsRead(@PathVariable String userId) {
        notificationService.markAllAsRead(userId);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Toutes les notifications marquées comme lues");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Supprime une notification
     */
    @DeleteMapping("/{notificationId}")
    @Operation(summary = "Supprimer une notification", 
               description = "Supprime définitivement une notification")
    public ResponseEntity<Map<String, String>> deleteNotification(@PathVariable String notificationId) {
        notificationService.deleteNotification(notificationId);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Notification supprimée");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Test: Envoie une notification de test
     */
    @PostMapping("/{userId}/test")
    @Operation(summary = "Notification de test", 
               description = "Crée une notification de test (développement uniquement)")
    public ResponseEntity<NotificationDTO> sendTestNotification(
            @PathVariable String userId,
            @RequestParam(defaultValue = "Test") String title,
            @RequestParam(defaultValue = "Ceci est une notification de test") String message) {
        
        Notification notification = notificationService.createNotification(
            userId, "SYSTEM", title, message, "🔔"
        );
        
        return ResponseEntity.ok(convertToDTO(notification));
    }
    
    /**
     * Convertit une Notification en NotificationDTO
     */
    private NotificationDTO convertToDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setType(notification.getType());
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setPriority(notification.getPriority());
        dto.setRead(notification.isRead());
        dto.setActionUrl(notification.getActionUrl());
        dto.setIcon(notification.getIcon());
        dto.setRelatedEntityId(notification.getRelatedEntityId());
        dto.setRelatedEntityType(notification.getRelatedEntityType());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setTimeAgo(notificationService.getTimeAgo(notification.getCreatedAt()));
        
        return dto;
    }
}