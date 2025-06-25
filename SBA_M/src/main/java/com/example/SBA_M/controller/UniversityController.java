package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.UniversityRequest;
import com.example.SBA_M.dto.response.ApiResponse;
import com.example.SBA_M.dto.response.UniversityResponse;
import com.example.SBA_M.entity.queries.UniversityDocument;
import com.example.SBA_M.service.UniversityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/universities")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class UniversityController {

    private final UniversityService universityService;

    @Operation(summary = "Get university by ID")
    @GetMapping("/{id}")
    public ApiResponse<UniversityDocument> getUniversityById(@PathVariable Integer id) {
        UniversityDocument university = universityService.getUniversityById(id);
        return ApiResponse.<UniversityDocument>builder()
                .code(1000)
                .message("University fetched successfully")
                .result(university)
                .build();
    }

    @Operation(summary = "Create a new university")
    @PostMapping
    public ApiResponse<UniversityResponse> createUniversity(@Valid @RequestBody UniversityRequest universityRequest) {
        UniversityResponse created = universityService.createUniversity(universityRequest);
        return ApiResponse.<UniversityResponse>builder()
                .code(1001)
                .message("University created successfully")
                .result(created)
                .build();
    }

    @Operation(summary = "Update a university")
    @PutMapping("/{id}")
    public ApiResponse<UniversityResponse> updateUniversity(
            @PathVariable Integer id,
            @Valid @RequestBody UniversityRequest universityRequest) {
        UniversityResponse updated = universityService.updateUniversity(id, universityRequest);
        return ApiResponse.<UniversityResponse>builder()
                .code(1002)
                .message("University updated successfully")
                .result(updated)
                .build();
    }

    @Operation(summary = "Delete a university")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUniversity(@PathVariable Integer id) {
        universityService.deleteUniversity(id);
        return ApiResponse.<Void>builder()
                .code(1003)
                .message("University deleted successfully")
                .build();
    }
}
