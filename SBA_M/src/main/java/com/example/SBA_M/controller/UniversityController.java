package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.UniversityRequest;
import com.example.SBA_M.dto.request.StatusUpdateRequest;
import com.example.SBA_M.dto.response.ApiResponse;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.dto.response.UniversityResponse;
import com.example.SBA_M.entity.commands.University;
import com.example.SBA_M.entity.queries.UniversityDocument;
import com.example.SBA_M.service.UniversityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("api/v1/universities")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UniversityController {

    private final UniversityService universityService;

    @Operation(summary = "Get all universities (search, pagination, sort)")
    @GetMapping
    public ApiResponse<PageResponse<UniversityResponse>> getAllUniversities(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "provinceId", required = false) Integer provinceId,
            @RequestParam(value = "includeCampuses", defaultValue = "false") Boolean includeCampuses) {
        PageResponse<UniversityResponse> result = universityService.getAllUniversities(search, page, size, sort, categoryId, provinceId, includeCampuses);
        return ApiResponse.<PageResponse<UniversityResponse>>builder()
                .code(1000)
                .message("Universities fetched successfully")
                .result(result)
                .build();
    }

    @Operation(summary = "Get university by ID")
    @GetMapping("/{id}")
    public ApiResponse<UniversityResponse> getUniversityById(@PathVariable Integer id) {
        UniversityResponse university = universityService.getUniversityById(id);
        return ApiResponse.<UniversityResponse>builder()
                .code(1000)
                .message("University fetched successfully")
                .result(university)
                .build();
    }

    @Operation(summary = "Create a new university")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<UniversityResponse> createUniversity(@ModelAttribute @Valid UniversityRequest universityRequest) {
        UniversityResponse created = universityService.createUniversity(universityRequest);
        return ApiResponse.<UniversityResponse>builder()
                .code(1001)
                .message("University created successfully")
                .result(created)
                .build();
    }

    @Operation(summary = "Update a university")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<UniversityResponse> updateUniversity(
            @PathVariable Integer id,
            @ModelAttribute @Valid UniversityRequest universityRequest) {
        UniversityResponse updated = universityService.updateUniversity(id, universityRequest);
        return ApiResponse.<UniversityResponse>builder()
                .code(1002)
                .message("University updated successfully")
                .result(updated)
                .build();
    }

    @Operation(summary = "Delete a university")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUniversity(@PathVariable Integer id) {
        universityService.deleteUniversity(id);
        return ApiResponse.<Void>builder()
                .code(1003)
                .message("University deleted successfully")
                .build();
    }

    @Operation(summary = "Update university status")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/status")
    public ApiResponse<UniversityResponse> updateUniversityStatus(
            @PathVariable Integer id,
            @RequestBody StatusUpdateRequest request) {
        UniversityResponse updated = universityService.updateUniversityStatus(id, request.getStatus());
        return ApiResponse.<UniversityResponse>builder()
                .code(1004)
                .message("University status updated successfully")
                .result(updated)
                .build();
    }

    @Operation(summary = "Get university by code")
    @GetMapping("/by-code/{universityCode}")
    public ApiResponse<UniversityResponse> getUniversityByCode(@PathVariable String universityCode) {
        UniversityResponse university = universityService.getUniversityByCode(universityCode);
        return ApiResponse.<UniversityResponse>builder()
                .code(1000)
                .message("University fetched successfully")
                .result(university)
                .build();
    }

    @Operation(summary = "Get university by name")
    @GetMapping("/by-name/{name}")
    public ApiResponse<UniversityResponse> getUniversityByName(@PathVariable String name) {
        UniversityResponse university = universityService.getUniversityByName(name);
        return ApiResponse.<UniversityResponse>builder()
                .code(1000)
                .message("University fetched successfully")
                .result(university)
                .build();
    }

    @Operation(summary = "Get university by short name")
    @GetMapping("/by-short-name/{shortName}")
    public ApiResponse<UniversityResponse> getUniversityByShortName(@PathVariable String shortName) {
        UniversityResponse university = universityService.getUniversityByShortName(shortName);
        return ApiResponse.<UniversityResponse>builder()
                .code(1000)
                .message("University fetched successfully")
                .result(university)
                .build();
    }

    @Operation(summary = "Get universities by province")
    @GetMapping("/by-province/{provinceId}")
    public ApiResponse<PageResponse<UniversityResponse>> getUniversitiesByProvince(
            @PathVariable Integer provinceId,
            @RequestParam(value = "includeMainCampusOnly", defaultValue = "false") Boolean includeMainCampusOnly,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false) String sort) {
        PageResponse<UniversityResponse> result = universityService.getUniversitiesByProvince(provinceId, includeMainCampusOnly, page, size, sort);
        return ApiResponse.<PageResponse<UniversityResponse>>builder()
                .code(1000)
                .message("Universities in province fetched successfully")
                .result(result)
                .build();
    }
}
