package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.SubjectCombinationRequest;
import com.example.SBA_M.dto.response.ApiResponse;
import com.example.SBA_M.dto.response.SubjectCombinationResponse;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.service.SubjectCombinationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.example.SBA_M.dto.request.StatusUpdateRequest;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/subject-combinations")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Subject Combination Controller", description = "APIs for managing subject combinations")
public class SubjectCombinationController {

    private final SubjectCombinationService subjectCombinationService;

    @Operation(summary = "Create a new subject combination")
    @PostMapping
    public ApiResponse<SubjectCombinationResponse> createSubjectCombination(
            @Valid @RequestBody SubjectCombinationRequest request) {
        SubjectCombinationResponse response = subjectCombinationService.createSubjectCombination(request);
        return ApiResponse.<SubjectCombinationResponse>builder()
                .code(1001)
                .message("Subject combination created successfully")
                .result(response)
                .build();
    }

    @Operation(summary = "Get subject combination by ID")
    @GetMapping("/{id}")
    public ApiResponse<SubjectCombinationResponse> getSubjectCombinationById(@PathVariable Long id) {
        SubjectCombinationResponse response = subjectCombinationService.getSubjectCombinationById(id);
        return ApiResponse.<SubjectCombinationResponse>builder()
                .code(1000)
                .message("Subject combination fetched successfully")
                .result(response)
                .build();
    }

    @Operation(summary = "Get all subject combinations (search, paging, sort)")
    @GetMapping
    public ApiResponse<PageResponse<SubjectCombinationResponse>> getAllSubjectCombinations(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "block", required = false) String block,
            @RequestParam(value = "examSubject", required = false) String examSubject) {
        PageResponse<SubjectCombinationResponse> responses = subjectCombinationService.getAllSubjectCombinations(search, page, size, sort, block, examSubject);
        return ApiResponse.<PageResponse<SubjectCombinationResponse>>builder()
                .code(1000)
                .message("List of subject combinations fetched successfully")
                .result(responses)
                .build();
    }

    @Operation(summary = "Update subject combination")
    @PutMapping("/{id}")
    public ApiResponse<SubjectCombinationResponse> updateSubjectCombination(
            @PathVariable Long id,
            @Valid @RequestBody SubjectCombinationRequest request) {
        SubjectCombinationResponse response = subjectCombinationService.updateSubjectCombination(id, request);
        return ApiResponse.<SubjectCombinationResponse>builder()
                .code(1002)
                .message("Subject combination updated successfully")
                .result(response)
                .build();
    }

    @Operation(summary = "Delete subject combination")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSubjectCombination(@PathVariable Long id) {
        subjectCombinationService.deleteSubjectCombination(id);
        return ApiResponse.<Void>builder()
                .code(1003)
                .message("Subject combination deleted successfully")
                .build();
    }

    @Operation(summary = "Update status of a subject combination")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/status")
    public ApiResponse<SubjectCombinationResponse> updateSubjectCombinationStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest request) {
        SubjectCombinationResponse updated = subjectCombinationService.updateSubjectCombinationStatus(id, request.getStatus());
        return ApiResponse.<SubjectCombinationResponse>builder()
                .code(1004)
                .message("Subject combination status updated successfully")
                .result(updated)
                .build();
    }
}

