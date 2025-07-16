package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.CampusTypeRequest;
import com.example.SBA_M.dto.request.StatusUpdateRequest;
import com.example.SBA_M.dto.response.ApiResponse;
import com.example.SBA_M.dto.response.CampusTypeResponse;
import com.example.SBA_M.service.CampusTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/campus-types")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CampusTypeController {

    private final CampusTypeService campusTypeService;

    @Operation(summary = "Get all campus types")
    @GetMapping
    public ApiResponse<List<CampusTypeResponse>> getAllCampusTypes() {
        List<CampusTypeResponse> campusTypes = campusTypeService.getAllCampusTypes();
        return ApiResponse.<List<CampusTypeResponse>>builder()
                .code(1000)
                .message("Campus types fetched successfully")
                .result(campusTypes)
                .build();
    }

    @Operation(summary = "Get campus type by ID")
    @GetMapping("/{id}")
    public ApiResponse<CampusTypeResponse> getCampusTypeById(@PathVariable Integer id) {
        CampusTypeResponse campusType = campusTypeService.getCampusTypeById(id);
        return ApiResponse.<CampusTypeResponse>builder()
                .code(1000)
                .message("Campus type fetched successfully")
                .result(campusType)
                .build();
    }

    @Operation(summary = "Create a new campus type")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ApiResponse<CampusTypeResponse> createCampusType(@Valid @RequestBody CampusTypeRequest campusTypeRequest) {
        CampusTypeResponse created = campusTypeService.createCampusType(campusTypeRequest);
        return ApiResponse.<CampusTypeResponse>builder()
                .code(1001)
                .message("Campus type created successfully")
                .result(created)
                .build();
    }

    @Operation(summary = "Update a campus type")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ApiResponse<CampusTypeResponse> updateCampusType(
            @PathVariable Integer id,
            @Valid @RequestBody CampusTypeRequest campusTypeRequest) {
        CampusTypeResponse updated = campusTypeService.updateCampusType(id, campusTypeRequest);
        return ApiResponse.<CampusTypeResponse>builder()
                .code(1002)
                .message("Campus type updated successfully")
                .result(updated)
                .build();
    }

    @Operation(summary = "Delete a campus type")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCampusType(@PathVariable Integer id) {
        campusTypeService.deleteCampusType(id);
        return ApiResponse.<Void>builder()
                .code(1003)
                .message("Campus type deleted successfully")
                .build();
    }

    @Operation(summary = "Update campus type status")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/status")
    public ApiResponse<CampusTypeResponse> updateCampusTypeStatus(
            @PathVariable Integer id,
            @RequestBody StatusUpdateRequest request) {
        CampusTypeResponse updated = campusTypeService.updateCampusTypeStatus(id, request.getStatus());
        return ApiResponse.<CampusTypeResponse>builder()
                .code(1004)
                .message("Campus type status updated successfully")
                .result(updated)
                .build();
    }

    @Operation(summary = "Search, paginate, and sort campus types")
    @GetMapping("/search")
    public ApiResponse<?> searchCampusTypes(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id,asc") String sort
    ) {
        var result = campusTypeService.searchCampusTypes(search, page, size, sort);
        return ApiResponse.builder()
                .code(1000)
                .message("Campus types fetched successfully")
                .result(result)
                .build();
    }
} 