package com.example.SBA_M.service.websocket;

import com.example.SBA_M.dto.response.ConsultationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Send notification when a new consultation is created
     */
    public void notifyNewConsultation(ConsultationResponse consultation) {
        try {
            UUID consultantId = consultation.getConsultant().getId();

            WebSocketNotification notification = WebSocketNotification.builder()
                    .type(NotificationType.NEW_CONSULTATION)
                    .message("New consultation received: " + consultation.getTitle())
                    .data(consultation)
                    .timestamp(Instant.now())
                    .build();

            // Send to specific consultant
            messagingTemplate.convertAndSend(
                    "/topic/consultant/" + consultantId,
                    notification
            );

            log.info("WebSocket notification sent for new consultation: {} to consultant: {}",
                    consultation.getId(), consultantId);
        } catch (Exception e) {
            log.error("Failed to send WebSocket notification for new consultation: {}",
                    consultation.getId(), e);
        }
    }

    /**
     * Send notification when a consultation is updated by user
     */
    public void notifyConsultationUpdated(ConsultationResponse consultation) {
        try {
            UUID consultantId = consultation.getConsultant().getId();

            WebSocketNotification notification = WebSocketNotification.builder()
                    .type(NotificationType.CONSULTATION_UPDATED)
                    .message("Consultation updated: " + consultation.getTitle())
                    .data(consultation)
                    .timestamp(Instant.now())
                    .build();

            messagingTemplate.convertAndSend(
                    "/topic/consultant/" + consultantId,
                    notification
            );

            log.info("WebSocket notification sent for consultation update: {} to consultant: {}",
                    consultation.getId(), consultantId);
        } catch (Exception e) {
            log.error("Failed to send WebSocket notification for consultation update: {}",
                    consultation.getId(), e);
        }
    }

    /**
     * Send notification when a consultation is answered by consultant
     */
    public void notifyConsultationAnswered(ConsultationResponse consultation) {
        try {
            UUID userId = consultation.getSender().getId();

            WebSocketNotification notification = WebSocketNotification.builder()
                    .type(NotificationType.CONSULTATION_ANSWERED)
                    .message("Your consultation has been answered: " + consultation.getTitle())
                    .data(consultation)
                    .timestamp(Instant.now())
                    .build();

            messagingTemplate.convertAndSend(
                    "/topic/user/" + userId,
                    notification
            );

            log.info("WebSocket notification sent for consultation answer: {} to user: {}",
                    consultation.getId(), userId);
        } catch (Exception e) {
            log.error("Failed to send WebSocket notification for consultation answer: {}",
                    consultation.getId(), e);
        }
    }

    /**
     * Send notification when a consultation answer is updated by consultant
     */
    public void notifyConsultationAnswerUpdated(ConsultationResponse consultation) {
        try {
            UUID userId = consultation.getSender().getId();

            WebSocketNotification notification = WebSocketNotification.builder()
                    .type(NotificationType.CONSULTATION_UPDATED)
                    .message("Your consultation answer has been updated: " + consultation.getTitle())
                    .data(consultation)
                    .timestamp(Instant.now())
                    .build();

            messagingTemplate.convertAndSend(
                    "/topic/user/" + userId,
                    notification
            );

            log.info("WebSocket notification sent for consultation answer update: {} to user: {}",
                    consultation.getId(), userId);
        } catch (Exception e) {
            log.error("Failed to send WebSocket notification for consultation answer update: {}",
                    consultation.getId(), e);
        }
    }

    /**
     * Send notification when a consultation is cancelled
     */
    public void notifyConsultationCancelled(UUID userId, Long consultationId, String title) {
        try {
            WebSocketNotification notification = WebSocketNotification.builder()
                    .type(NotificationType.CONSULTATION_CANCELLED)
                    .message("Consultation cancelled: " + title)
                    .data(consultationId)
                    .timestamp(Instant.now())
                    .build();

            messagingTemplate.convertAndSend(
                    "/topic/user/" + userId,
                    notification
            );

            log.info("WebSocket notification sent for consultation cancellation: {} to user: {}",
                    consultationId, userId);
        } catch (Exception e) {
            log.error("Failed to send WebSocket notification for consultation cancellation: {}",
                    consultationId, e);
        }
    }

    /**
     * Broadcast general consultation statistics update (optional)
     */
    public void broadcastConsultationStats(Object stats) {
        try {
            WebSocketNotification notification = WebSocketNotification.builder()
                    .type(NotificationType.STATS_UPDATE)
                    .message("Consultation statistics updated")
                    .data(stats)
                    .timestamp(Instant.now())
                    .build();

            messagingTemplate.convertAndSend("/topic/consultation/stats", notification);

            log.info("WebSocket notification sent for consultation statistics update");
        } catch (Exception e) {
            log.error("Failed to send WebSocket notification for consultation statistics", e);
        }
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
        private Instant timestamp;
    }

    public enum NotificationType {
        NEW_CONSULTATION,
        CONSULTATION_ANSWERED,
        CONSULTATION_UPDATED,
        CONSULTATION_CANCELLED,
        STATS_UPDATE
    }
}