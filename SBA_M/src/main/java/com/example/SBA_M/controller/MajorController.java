package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.MajorRequest;
import com.example.SBA_M.dto.response.ApiResponse;
import com.example.SBA_M.dto.response.MajorResponse;
import com.example.SBA_M.service.MajorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.dto.request.StatusUpdateRequest;

@RestController
@RequestMapping("/api/v1/majors")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Major Controller", description = "APIs for managing majors")
public class MajorController {

    private final MajorService majorService;

    @Operation(summary = "Create a new major")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ApiResponse<MajorResponse> createMajor(@Valid @RequestBody MajorRequest request) {
        MajorResponse createdMajor = majorService.createMajor(request);
        return ApiResponse.<MajorResponse>builder()
                .code(1001)
                .message("Major created successfully")
                .result(createdMajor)
                .build();
    }

    @Operation(summary = "Get major by ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ApiResponse<MajorResponse> getMajorById(@PathVariable Long id) {
        MajorResponse major = majorService.getMajorById(id);
        return ApiResponse.<MajorResponse>builder()
                .code(1000)
                .message("Major fetched successfully")
                .result(major)
                .build();
    }

    @Operation(summary = "Get all majors (search, paging, sort)")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    public ApiResponse<PageResponse<MajorResponse>> getAllMajors(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false) String sort
    ) {
        PageResponse<MajorResponse> majors = majorService.getAllMajors(search, page, size, sort);
        return ApiResponse.<PageResponse<MajorResponse>>builder()
                .code(1000)
                .message("List of majors fetched successfully")
                .result(majors)
                .build();
    }

    @Operation(summary = "Update a major")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ApiResponse<MajorResponse> updateMajor(
            @PathVariable Long id,
            @Valid @RequestBody MajorRequest request) {
        MajorResponse updatedMajor = majorService.updateMajor(id, request);
        return ApiResponse.<MajorResponse>builder()
                .code(1002)
                .message("Major updated successfully")
                .result(updatedMajor)
                .build();
    }

    @Operation(summary = "Update status of a major")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/status")
    public ApiResponse<MajorResponse> updateMajorStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest request) {
        MajorResponse updated = majorService.updateMajorStatus(id, request.getStatus());
        return ApiResponse.<MajorResponse>builder()
                .code(1004)
                .message("Major status updated successfully")
                .result(updated)
                .build();
    }

    @Operation(summary = "Delete a major")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMajor(@PathVariable Long id) {
        majorService.deleteMajor(id);
        return ApiResponse.<Void>builder()
                .code(1003)
                .message("Major deleted successfully")
                .build();
    }
}
