package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.ConsultantProfileRequest;
import com.example.SBA_M.dto.response.ApiResponse;
import com.example.SBA_M.dto.response.ConsultantProfileResponse;
import com.example.SBA_M.service.ConsultantProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/consultant-profiles")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ConsultantProfileController {

    private final ConsultantProfileService consultantProfileService;

    @Operation(summary = "Create a consultant profile")
    @PreAuthorize("hasAnyRole('CONSULTANT', 'ADMIN')")
    @PostMapping
    public ApiResponse<ConsultantProfileResponse> createProfile(
            @Valid @RequestBody ConsultantProfileRequest request
    ) {
        ConsultantProfileResponse response = consultantProfileService.create(request);
        return ApiResponse.<ConsultantProfileResponse>builder()
                .code(1001)
                .message("Consultant profile created successfully")
                .result(response)
                .build();
    }

    @Operation(summary = "Update a consultant profile")
    @PreAuthorize("hasAnyRole('CONSULTANT', 'ADMIN')")
    @PutMapping("/{id}")
    public ApiResponse<ConsultantProfileResponse> updateProfile(
            @PathVariable Integer id,
            @Valid @RequestBody ConsultantProfileRequest request
    ) {
        ConsultantProfileResponse response = consultantProfileService.update(id, request);
        return ApiResponse.<ConsultantProfileResponse>builder()
                .code(1002)
                .message("Consultant profile updated successfully")
                .result(response)
                .build();
    }

    @Operation(summary = "Delete a consultant profile")
    @PreAuthorize("hasAnyRole('CONSULTANT', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProfile(
            @PathVariable Integer id
    ) {
        consultantProfileService.delete(id);
        return ApiResponse.<Void>builder()
                .code(1003)
                .message("Consultant profile deleted successfully")
                .build();
    }

    @Operation(summary = "Get a consultant profile by ID")
    @PreAuthorize("hasAnyRole('CONSULTANT', 'ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ApiResponse<ConsultantProfileResponse> getProfileById(
            @PathVariable Integer id
    ) {
        ConsultantProfileResponse response = consultantProfileService.getById(id);
        return ApiResponse.<ConsultantProfileResponse>builder()
                .code(1000)
                .message("Consultant profile fetched successfully")
                .result(response)
                .build();
    }

    @Operation(summary = "Get all consultant profiles (paginated)")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    public ApiResponse<Page<ConsultantProfileResponse>> getAllProfiles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ConsultantProfileResponse> response = consultantProfileService.getAll(
                org.springframework.data.domain.PageRequest.of(page, size)
        );
        return ApiResponse.<Page<ConsultantProfileResponse>>builder()
                .code(1000)
                .message("Consultant profiles fetched successfully")
                .result(response)
                .build();
    }

    @Operation(summary = "Search consultant profiles by keyword (bio, etc.)")
    @PreAuthorize("hasAnyRole('CONSULTANT', 'ADMIN', 'USER')")
    @GetMapping("/search")
    public ApiResponse<Page<ConsultantProfileResponse>> searchProfiles(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ConsultantProfileResponse> response = consultantProfileService.search(
                keyword,
                org.springframework.data.domain.PageRequest.of(page, size)
        );
        return ApiResponse.<Page<ConsultantProfileResponse>>builder()
                .code(1000)
                .message("Consultant profiles search successful")
                .result(response)
                .build();
    }
}

