package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.ConsultationAnswerRequest;
import com.example.SBA_M.dto.request.ConsultationCreateRequest;
import com.example.SBA_M.dto.response.ApiResponse;
import com.example.SBA_M.dto.response.ConsultationResponse;
import com.example.SBA_M.service.ConsultationService;
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
        return ApiResponse.<ConsultationResponse>builder()
                .code(1002)
                .message("Consultation updated successfully.")
                .result(response)
                .build();
    }

    @Operation(summary = "User views all their consultations (paginated)")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/user/{userId}")
    public ApiResponse<Page<ConsultationResponse>> getUserConsultations(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ConsultationResponse> response = consultationService.getUserConsultations(
                userId, PageRequest.of(page, size)
        );
        return ApiResponse.<Page<ConsultationResponse>>builder()
                .code(1000)
                .message("User consultations fetched successfully.")
                .result(response)
                .build();
    }

    @Operation(summary = "User searches their consultations by keyword")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/user/{userId}/search")
    public ApiResponse<Page<ConsultationResponse>> searchUserConsultations(
            @PathVariable UUID userId,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ConsultationResponse> response = consultationService.searchUserConsultations(
                userId, keyword, PageRequest.of(page, size)
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
    @PostMapping("/consultant/{consultantId}/answer")
    public ApiResponse<ConsultationResponse> answerConsultation(
            @PathVariable UUID consultantId,
            @Valid @RequestBody ConsultationAnswerRequest request
    ) {
        ConsultationResponse response = consultationService.answerConsultation(consultantId, request);
        return ApiResponse.<ConsultationResponse>builder()
                .code(1001)
                .message("Consultation answered successfully.")
                .result(response)
                .build();
    }

    @Operation(summary = "Consultant updates their answer for a consultation")
    @PreAuthorize("hasAnyRole('CONSULTANT', 'ADMIN')")
    @PutMapping("/consultant/{consultantId}/answer")
    public ApiResponse<ConsultationResponse> updateConsultantAnswer(
            @PathVariable UUID consultantId,
            @Valid @RequestBody ConsultationAnswerRequest request
    ) {
        ConsultationResponse response = consultationService.updateConsultantAnswer(consultantId, request);
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
        consultationService.cancelConsultation(consultationId);
        return ApiResponse.<Void>builder()
                .code(1003)
                .message("Consultation canceled successfully by consultant.")
                .build();
    }

    @Operation(summary = "Consultant views all their consultations (paginated)")
    @PreAuthorize("hasAnyRole('CONSULTANT', 'ADMIN')")
    @GetMapping("/consultant/{consultantId}")
    public ApiResponse<Page<ConsultationResponse>> getConsultantConsultations(
            @PathVariable UUID consultantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ConsultationResponse> response = consultationService.getConsultantConsultations(
                consultantId, PageRequest.of(page, size)
        );
        return ApiResponse.<Page<ConsultationResponse>>builder()
                .code(1000)
                .message("Consultant consultations fetched successfully.")
                .result(response)
                .build();
    }

    @Operation(summary = "Consultant searches consultations by keyword")
    @PreAuthorize("hasAnyRole('CONSULTANT', 'ADMIN')")
    @GetMapping("/consultant/{consultantId}/search")
    public ApiResponse<Page<ConsultationResponse>> searchConsultantConsultations(
            @PathVariable UUID consultantId,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ConsultationResponse> response = consultationService.searchConsultantConsultations(
                consultantId, keyword, PageRequest.of(page, size)
        );
        return ApiResponse.<Page<ConsultationResponse>>builder()
                .code(1000)
                .message("Consultant consultation search successful.")
                .result(response)
                .build();
    }
}

