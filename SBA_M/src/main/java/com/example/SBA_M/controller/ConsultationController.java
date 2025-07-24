package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.ConsultationAnswerRequest;
import com.example.SBA_M.dto.request.ConsultationCreateRequest;
import com.example.SBA_M.dto.response.ApiResponse;
import com.example.SBA_M.dto.response.ConsultationResponse;
import com.example.SBA_M.dto.response.GroupedConsultationResponse;
import com.example.SBA_M.service.ConsultationService;
import com.example.SBA_M.service.websocket.WebSocketNotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/consultations")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ConsultationController {

    private final ConsultationService consultationService;
    private final WebSocketNotificationService webSocketNotificationService;

    // --------------------------
    // USER ENDPOINTS
    // --------------------------

    @Operation(summary = "User creates a new consultation request")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping
    public ApiResponse<ConsultationResponse> createConsultation(
            @Valid @RequestBody ConsultationCreateRequest request
    ) {
        ConsultationResponse response = consultationService.createConsultation(request);

        // Send WebSocket notification to consultant
        webSocketNotificationService.notifyNewConsultation(response);

        return ApiResponse.<ConsultationResponse>builder()
                .code(1001)
                .message("Consultation created successfully.")
                .result(response)
                .build();
    }

    @Operation(summary = "User updates their pending consultation")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/{id}")
    public ApiResponse<ConsultationResponse> updateConsultation(
            @PathVariable Long id,
            @Valid @RequestBody ConsultationCreateRequest request
    ) {
        ConsultationResponse response = consultationService.updateConsultation(id, request);

        // Send WebSocket notification to consultant about update
        webSocketNotificationService.notifyConsultationUpdated(response);

        return ApiResponse.<ConsultationResponse>builder()
                .code(1002)
                .message("Consultation updated successfully.")
                .result(response)
                .build();
    }

    @Operation(summary = "User views all their consultations (paginated)")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/user/{consultantId}")
    public ApiResponse<Page<ConsultationResponse>> getUserConsultations(
            @PathVariable UUID consultantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ConsultationResponse> response = consultationService.getUserConsultations(
                consultantId, PageRequest.of(page, size)
        );
        return ApiResponse.<Page<ConsultationResponse>>builder()
                .code(1000)
                .message("User consultations fetched successfully.")
                .result(response)
                .build();
    }

    @Operation(summary = "User searches their consultations by keyword")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/user/{consultantId}/search")
    public ApiResponse<Page<ConsultationResponse>> searchUserConsultations(
            @PathVariable UUID consultantId,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ConsultationResponse> response = consultationService.searchUserConsultations(
                consultantId, keyword, PageRequest.of(page, size)
        );
        return ApiResponse.<Page<ConsultationResponse>>builder()
                .code(1000)
                .message("User consultation search successful.")
                .result(response)
                .build();
    }

    // --------------------------
    // CONSULTANT ENDPOINTS
    // --------------------------

    @Operation(summary = "Consultant answers a consultation (new answer)")
    @PreAuthorize("hasAnyRole('CONSULTANT', 'ADMIN')")
    @PostMapping("/consultant/answer")
    public ApiResponse<ConsultationResponse> answerConsultation(
            @Valid @RequestBody ConsultationAnswerRequest request
    ) {
        ConsultationResponse response = consultationService.answerConsultation(request);

        // Send WebSocket notification to user about answer
        webSocketNotificationService.notifyConsultationAnswered(response);

        return ApiResponse.<ConsultationResponse>builder()
                .code(1001)
                .message("Consultation answered successfully.")
                .result(response)
                .build();
    }

    @Operation(summary = "Consultant updates their answer for a consultation")
    @PreAuthorize("hasAnyRole('CONSULTANT', 'ADMIN')")
    @PutMapping("/consultant/answer")
    public ApiResponse<ConsultationResponse> updateConsultantAnswer(
            @Valid @RequestBody ConsultationAnswerRequest request
    ) {
        ConsultationResponse response = consultationService.updateConsultantAnswer(request);

        // Send WebSocket notification to user about answer update
        webSocketNotificationService.notifyConsultationAnswerUpdated(response);

        return ApiResponse.<ConsultationResponse>builder()
                .code(1002)
                .message("Consultation answer updated successfully.")
                .result(response)
                .build();
    }

    @Operation(summary = "Consultant cancels (soft delete) a consultation")
    @PreAuthorize("hasAnyRole('CONSULTANT', 'ADMIN')")
    @DeleteMapping("/consultant/{consultationId}")
    public ApiResponse<Void> cancelConsultation(
            @PathVariable Long consultationId
    ) {
        // You'll need to get consultation details before canceling to send notification
        ConsultationResponse consultation = consultationService.getConsultationById(consultationId);

        consultationService.cancelConsultation(consultationId);

        // Send WebSocket notification to user about cancellation
        webSocketNotificationService.notifyConsultationCancelled(
                consultation.getSender().getId(),
                consultationId,
                consultation.getTitle()
        );

        return ApiResponse.<Void>builder()
                .code(1003)
                .message("Consultation canceled successfully by consultant.")
                .build();
    }

    @Operation(summary = "Consultant views all their consultations (paginated)")
    @PreAuthorize("hasAnyRole('CONSULTANT', 'ADMIN')")
    @GetMapping("/consultant")
    public ApiResponse<Page<GroupedConsultationResponse>> getConsultantConsultations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<GroupedConsultationResponse> response = consultationService.getConsultantConsultations(
                PageRequest.of(page, size)
        );
        return ApiResponse.<Page<GroupedConsultationResponse>>builder()
                .code(1000)
                .message("Consultant consultations fetched successfully.")
                .result(response)
                .build();
    }

    @Operation(summary = "Consultant searches consultations by keyword")
    @PreAuthorize("hasAnyRole('CONSULTANT', 'ADMIN')")
    @GetMapping("/consultant/search")
    public ApiResponse<Page<ConsultationResponse>> searchConsultantConsultations(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ConsultationResponse> response = consultationService.searchConsultantConsultations(
                keyword, PageRequest.of(page, size)
        );
        return ApiResponse.<Page<ConsultationResponse>>builder()
                .code(1000)
                .message("Consultant consultation search successful.")
                .result(response)
                .build();
    }
}