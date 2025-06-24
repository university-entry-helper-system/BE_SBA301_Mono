package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.SubjectCombinationRequest;
import com.example.SBA_M.dto.response.ApiResponse;
import com.example.SBA_M.dto.response.SubjectCombinationResponse;
import com.example.SBA_M.service.SubjectCombinationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subject-combinations")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
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
    public ApiResponse<SubjectCombinationResponse> getSubjectCombinationById(
            @PathVariable Long id) {
        SubjectCombinationResponse response = subjectCombinationService.getSubjectCombinationById(id);
        return ApiResponse.<SubjectCombinationResponse>builder()
                .code(1000)
                .message("Subject combination fetched successfully")
                .result(response)
                .build();
    }

    @Operation(summary = "Get all subject combinations")
    @GetMapping
    public ApiResponse<List<SubjectCombinationResponse>> getAllSubjectCombinations() {
        List<SubjectCombinationResponse> responses = subjectCombinationService.getAllSubjectCombinations();
        return ApiResponse.<List<SubjectCombinationResponse>>builder()
                .code(1000)
                .message("All subject combinations fetched successfully")
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
}
