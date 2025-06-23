package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.ExamSubjectRequest;
import com.example.SBA_M.dto.response.ExamSubjectResponse;
import com.example.SBA_M.service.ExamSubjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/exam-subjects")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Exam Subject Controller", description = "APIs for managing exam subjects")
public class ExamSubjectController {

    private final ExamSubjectService examSubjectService;

    @Operation(summary = "Create a new exam subject", description = "Create a new exam subject with the provided data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Exam subject created successfully",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ExamSubjectResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping
    public ResponseEntity<ExamSubjectResponse> createExamSubject(@Valid @RequestBody ExamSubjectRequest request) {
        ExamSubjectResponse createdSubject = examSubjectService.createExamSubject(request);
        return new ResponseEntity<>(createdSubject, HttpStatus.CREATED);
    }

    @Operation(summary = "Get exam subject by ID", description = "Retrieve an exam subject by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the exam subject",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ExamSubjectResponse.class))),
        @ApiResponse(responseCode = "404", description = "Exam subject not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ExamSubjectResponse> getExamSubjectById(@PathVariable Long id) {
        ExamSubjectResponse subject = examSubjectService.getExamSubjectById(id);
        return ResponseEntity.ok(subject);
    }

    @Operation(summary = "Get all exam subjects", description = "Retrieve a list of all exam subjects")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of exam subjects retrieved successfully",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping
    public ResponseEntity<List<ExamSubjectResponse>> getAllExamSubjects() {
        List<ExamSubjectResponse> subjects = examSubjectService.getAllExamSubjects();
        return ResponseEntity.ok(subjects);
    }

    @Operation(summary = "Update an exam subject", description = "Update an existing exam subject with the provided data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Exam subject updated successfully",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ExamSubjectResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Exam subject not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ExamSubjectResponse> updateExamSubject(
            @PathVariable Long id,
            @Valid @RequestBody ExamSubjectRequest request) {
        ExamSubjectResponse updatedSubject = examSubjectService.updateExamSubject(id, request);
        return ResponseEntity.ok(updatedSubject);
    }

    @Operation(summary = "Delete an exam subject", description = "Delete an exam subject by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Exam subject deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Exam subject not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteExamSubject(@PathVariable Long id) {
        examSubjectService.deleteExamSubject(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Exam subject deleted successfully");
        return ResponseEntity.ok(response);
    }
}