package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.CampusRequest;
import com.example.SBA_M.dto.request.StatusUpdateRequest;
import com.example.SBA_M.dto.response.ApiResponse;
import com.example.SBA_M.dto.response.CampusResponse;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.service.CampusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/campuses")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CampusController {

    private final CampusService campusService;

    @Operation(summary = "Get all campuses (search, pagination, sort)")
    @GetMapping
    public ApiResponse<PageResponse<CampusResponse>> getAllCampuses(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "universityId", required = false) Integer universityId,
            @RequestParam(value = "provinceId", required = false) Integer provinceId,
            @RequestParam(value = "campusType", required = false) String campusType,
            @RequestParam(value = "isMainCampus", required = false) Boolean isMainCampus) {
        PageResponse<CampusResponse> result = campusService.getAllCampuses(search, page, size, sort, universityId, provinceId, campusType, isMainCampus);
        return ApiResponse.<PageResponse<CampusResponse>>builder()
                .code(1000)
                .message("Campuses fetched successfully")
                .result(result)
                .build();
    }

    @Operation(summary = "Get campus by ID")
    @GetMapping("/{id}")
    public ApiResponse<CampusResponse> getCampusById(@PathVariable Integer id) {
        CampusResponse campus = campusService.getCampusById(id);
        return ApiResponse.<CampusResponse>builder()
                .code(1000)
                .message("Campus fetched successfully")
                .result(campus)
                .build();
    }

    @Operation(summary = "Create a new campus")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ApiResponse<CampusResponse> createCampus(@Valid @RequestBody CampusRequest campusRequest) {
        CampusResponse created = campusService.createCampus(campusRequest);
        return ApiResponse.<CampusResponse>builder()
                .code(1001)
                .message("Campus created successfully")
                .result(created)
                .build();
    }

    @Operation(summary = "Update a campus")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ApiResponse<CampusResponse> updateCampus(
            @PathVariable Integer id,
            @Valid @RequestBody CampusRequest campusRequest) {
        CampusResponse updated = campusService.updateCampus(id, campusRequest);
        return ApiResponse.<CampusResponse>builder()
                .code(1002)
                .message("Campus updated successfully")
                .result(updated)
                .build();
    }

    @Operation(summary = "Delete a campus")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCampus(@PathVariable Integer id) {
        campusService.deleteCampus(id);
        return ApiResponse.<Void>builder()
                .code(1003)
                .message("Campus deleted successfully")
                .build();
    }

    @Operation(summary = "Update campus status")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/status")
    public ApiResponse<CampusResponse> updateCampusStatus(
            @PathVariable Integer id,
            @RequestBody StatusUpdateRequest request) {
        CampusResponse updated = campusService.updateCampusStatus(id, request.getStatus());
        return ApiResponse.<CampusResponse>builder()
                .code(1004)
                .message("Campus status updated successfully")
                .result(updated)
                .build();
    }

    @Operation(summary = "Get campuses by university")
    @GetMapping("/by-university/{universityId}")
    public ApiResponse<List<CampusResponse>> getCampusesByUniversity(
            @PathVariable Integer universityId,
            @RequestParam(value = "includeInactive", defaultValue = "false") Boolean includeInactive) {
        List<CampusResponse> campuses = campusService.getCampusesByUniversity(universityId, includeInactive);
        return ApiResponse.<List<CampusResponse>>builder()
                .code(1000)
                .message("Campuses by university fetched successfully")
                .result(campuses)
                .build();
    }

    @Operation(summary = "Get campuses by province")
    @GetMapping("/by-province/{provinceId}")
    public ApiResponse<PageResponse<CampusResponse>> getCampusesByProvince(
            @PathVariable Integer provinceId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false) String sort) {
        PageResponse<CampusResponse> result = campusService.getCampusesByProvince(provinceId, page, size, sort);
        return ApiResponse.<PageResponse<CampusResponse>>builder()
                .code(1000)
                .message("Campuses by province fetched successfully")
                .result(result)
                .build();
    }
} 