package com.example.SBA_M.controller;

import com.example.SBA_M.dto.response.ConsultationResponse;
import com.example.SBA_M.utils.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketConsultationController {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Handle client subscription acknowledgment
     */
    @MessageMapping("/consultation/subscribe")
    public void handleSubscription(@Payload String message, Principal principal) {
        log.info("User {} subscribed to consultation updates: {}", principal.getName(), message);
        // You can send an acknowledgment back if needed
        messagingTemplate.convertAndSendToUser(
                principal.getName(),
                "/topic/consultation/ack",
                "Subscribed successfully"
        );
    }

    /**
     * Notify consultant about new consultation
     */
    public void notifyConsultantNewConsultation(UUID consultantId, ConsultationResponse consultation) {
        log.info("Notifying consultant {} about new consultation {}", consultantId, consultation.getId());

        WebSocketNotification notification = WebSocketNotification.builder()
                .type(NotificationType.NEW_CONSULTATION)
                .message("New consultation received: " + consultation.getTitle())
                .data(consultation)
                .timestamp(java.time.Instant.now())
                .build();

        messagingTemplate.convertAndSend(
                "/topic/consultant/" + consultantId,
                notification
        );
    }

    /**
     * Notify user about consultation update (answer, status change)
     */
    public void notifyUserConsultationUpdate(UUID userId, ConsultationResponse consultation) {
        log.info("Notifying user {} about consultation update {}", userId, consultation.getId());

        WebSocketNotification notification = WebSocketNotification.builder()
                .type(NotificationType.CONSULTATION_ANSWERED)
                .message("Your consultation has been answered: " + consultation.getTitle())
                .data(consultation)
                .timestamp(java.time.Instant.now())
                .build();

        messagingTemplate.convertAndSend(
                "/topic/user/" + userId,
                notification
        );
    }

    /**
     * Notify consultant about consultation update from user
     */
    public void notifyConsultantConsultationUpdate(UUID consultantId, ConsultationResponse consultation) {
        log.info("Notifying consultant {} about consultation update {}", consultantId, consultation.getId());

        WebSocketNotification notification = WebSocketNotification.builder()
                .type(NotificationType.CONSULTATION_UPDATED)
                .message("Consultation updated: " + consultation.getTitle())
                .data(consultation)
                .timestamp(java.time.Instant.now())
                .build();

        messagingTemplate.convertAndSend(
                "/topic/consultant/" + consultantId,
                notification
        );
    }

    /**
     * Notify user about consultation cancellation
     */
    public void notifyUserConsultationCancelled(UUID userId, Long consultationId, String title) {
        log.info("Notifying user {} about consultation cancellation {}", userId, consultationId);

        WebSocketNotification notification = WebSocketNotification.builder()
                .type(NotificationType.CONSULTATION_CANCELLED)
                .message("Consultation cancelled: " + title)
                .data(consultationId)
                .timestamp(java.time.Instant.now())
                .build();

        messagingTemplate.convertAndSend(
                "/topic/user/" + userId,
                notification
        );
    }

    /**
     * Broadcast general consultation statistics update (optional)
     */
    public void broadcastConsultationStats(Object stats) {
        log.info("Broadcasting consultation statistics update");

        WebSocketNotification notification = WebSocketNotification.builder()
                .type(NotificationType.STATS_UPDATE)
                .message("Consultation statistics updated")
                .data(stats)
                .timestamp(java.time.Instant.now())
                .build();

        messagingTemplate.convertAndSend("/topic/consultation/stats", notification);
    }

    // Inner classes for notification structure

    @lombok.Data
    @lombok.Builder
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    public static class WebSocketNotification {
        private NotificationType type;
        private String message;
        private Object data;
        private java.time.Instant timestamp;
    }


}