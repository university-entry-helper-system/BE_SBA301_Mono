package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.ExamSubjectRequest;
import com.example.SBA_M.dto.request.StatusUpdateRequest;
import com.example.SBA_M.dto.response.ApiResponse;
import com.example.SBA_M.dto.response.ExamSubjectResponse;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.service.ExamSubjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/exam-subjects")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Exam Subject Controller", description = "APIs for managing exam subjects")
public class ExamSubjectController {

    private final ExamSubjectService examSubjectService;

    @Operation(summary = "Create a new exam subject")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ApiResponse<ExamSubjectResponse> createExamSubject(@Valid @RequestBody ExamSubjectRequest request) {
        ExamSubjectResponse createdSubject = examSubjectService.createExamSubject(request);
        return ApiResponse.<ExamSubjectResponse>builder()
                .code(1001)
                .message("Exam subject created successfully")
                .result(createdSubject)
                .build();
    }

    @Operation(summary = "Get exam subject by ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ApiResponse<ExamSubjectResponse> getExamSubjectById(@PathVariable Long id) {
        ExamSubjectResponse subject = examSubjectService.getExamSubjectById(id);
        return ApiResponse.<ExamSubjectResponse>builder()
                .code(1000)
                .message("Exam subject fetched successfully")
                .result(subject)
                .build();
    }

    @Operation(summary = "Get all exam subjects (search, paging, sort)")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    public ApiResponse<PageResponse<ExamSubjectResponse>> getAllExamSubjects(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false) String sort) {
        PageResponse<ExamSubjectResponse> subjects = examSubjectService.getAllExamSubjects(search, page, size, sort);
        return ApiResponse.<PageResponse<ExamSubjectResponse>>builder()
                .code(1000)
                .message("List of exam subjects fetched successfully")
                .result(subjects)
                .build();
    }

    @Operation(summary = "Update an exam subject")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ApiResponse<ExamSubjectResponse> updateExamSubject(
            @PathVariable Long id,
            @Valid @RequestBody ExamSubjectRequest request) {
        ExamSubjectResponse updatedSubject = examSubjectService.updateExamSubject(id, request);
        return ApiResponse.<ExamSubjectResponse>builder()
                .code(1002)
                .message("Exam subject updated successfully")
                .result(updatedSubject)
                .build();
    }

    @Operation(summary = "Delete an exam subject")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteExamSubject(@PathVariable Long id) {
        examSubjectService.deleteExamSubject(id);
        return ApiResponse.<Void>builder()
                .code(1003)
                .message("Exam subject deleted successfully")
                .build();
    }

    @Operation(summary = "Update status of an exam subject")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/status")
    public ApiResponse<ExamSubjectResponse> updateExamSubjectStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest request) {
        ExamSubjectResponse updated = examSubjectService.updateExamSubjectStatus(id, request.getStatus());
        return ApiResponse.<ExamSubjectResponse>builder()
                .code(1004)
                .message("Exam subject status updated successfully")
                .result(updated)
                .build();
    }
}

