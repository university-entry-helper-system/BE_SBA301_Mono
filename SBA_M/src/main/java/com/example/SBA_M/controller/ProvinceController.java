package com.example.SBA_M.controller;

import com.example.SBA_M.dto.response.ApiResponse;
import com.example.SBA_M.dto.response.ProvinceResponse;
import com.example.SBA_M.service.ProvinceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/provinces")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Province Controller", description = "APIs for managing provinces")
public class ProvinceController {

    private final ProvinceService provinceService;

    @Operation(summary = "Create a new province")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ApiResponse<ProvinceResponse> createProvince(@Valid @RequestBody ProvinceResponse request) {
        ProvinceResponse createdProvince = provinceService.createProvince(request);
        return ApiResponse.<ProvinceResponse>builder()
                .code(1001)
                .message("Province created successfully")
                .result(createdProvince)
                .build();
    }

    @Operation(summary = "Get province by ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ApiResponse<ProvinceResponse> getProvinceById(@PathVariable Integer id) {
        ProvinceResponse province = provinceService.getProvinceById(id);
        return ApiResponse.<ProvinceResponse>builder()
                .code(1000)
                .message("Province fetched successfully")
                .result(province)
                .build();
    }

    @Operation(summary = "Get all provinces")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    public ApiResponse<List<ProvinceResponse>> getAllProvinces() {
        List<ProvinceResponse> provinces = provinceService.getAllProvinces();
        return ApiResponse.<List<ProvinceResponse>>builder()
                .code(1000)
                .message("List of provinces fetched successfully")
                .result(provinces)
                .build();
    }

    @Operation(summary = "Update a province")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ApiResponse<ProvinceResponse> updateProvince(
            @PathVariable Integer id,
            @Valid @RequestBody ProvinceResponse request) {
        ProvinceResponse updatedProvince = provinceService.updateProvince(id, request);
        return ApiResponse.<ProvinceResponse>builder()
                .code(1002)
                .message("Province updated successfully")
                .result(updatedProvince)
                .build();
    }

    @Operation(summary = "Delete a province")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProvince(@PathVariable Integer id) {
        provinceService.deleteProvince(id);
        return ApiResponse.<Void>builder()
                .code(1003)
                .message("Province deleted successfully")
                .build();
    }
}

