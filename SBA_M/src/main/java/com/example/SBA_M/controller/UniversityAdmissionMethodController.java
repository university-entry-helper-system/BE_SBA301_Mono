package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.UniversityMethodRequest;
import com.example.SBA_M.dto.response.*;
import com.example.SBA_M.service.UniversityAdmissionMethodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/university-admission-methods")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UniversityAdmissionMethodController {

    private final UniversityAdmissionMethodService universityAdmissionMethodService;

    @Operation(summary = "Get all university admission methods (paginated)")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ApiResponse<PageResponse<UniversityAdmissionMethodResponse>> getAllUniversityAdmissionMethods(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<UniversityAdmissionMethodResponse> result = universityAdmissionMethodService.getAll(page, size);
        return ApiResponse.<PageResponse<UniversityAdmissionMethodResponse>>builder()
                .code(1000)
                .message("University admission methods fetched successfully")
                .result(result)
                .build();
    }

    @Operation(summary = "Get university admission method by ID")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    public ApiResponse<UniversityAdmissionMethodResponse> getById(@PathVariable Integer id) {
        UniversityAdmissionMethodResponse response = universityAdmissionMethodService.getById(id);
        return ApiResponse.<UniversityAdmissionMethodResponse>builder()
                .code(1000)
                .message("University admission method fetched successfully")
                .result(response)
                .build();
    }

    @Operation(summary = "Create a new university admission method")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ApiResponse<UniversityAdmissionMethodResponse> create(
            @Valid @RequestBody UniversityMethodRequest request
    ) {
        UniversityAdmissionMethodResponse created = universityAdmissionMethodService.create(request);
        return ApiResponse.<UniversityAdmissionMethodResponse>builder()
                .code(1001)
                .message("University admission method created successfully")
                .result(created)
                .build();
    }

    @Operation(summary = "Update a university admission method")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ApiResponse<UniversityAdmissionMethodResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody UniversityMethodRequest request
    ) {
        UniversityAdmissionMethodResponse updated = universityAdmissionMethodService.update(id, request);
        return ApiResponse.<UniversityAdmissionMethodResponse>builder()
                .code(1002)
                .message("University admission method updated successfully")
                .result(updated)
                .build();
    }

    @Operation(summary = "Delete a university admission method")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Integer id) {
        universityAdmissionMethodService.delete(id);
        return ApiResponse.<Void>builder()
                .code(1003)
                .message("University admission method deleted successfully")
                .build();
    }

    @Operation(summary = "Get list of schools using a specific admission method (latest year only)")
    @GetMapping("/methods/{methodId}")
    public ApiResponse<List<UniversityAdmissionMethodSummaryResponse>> getSchoolsByMethod(
            @PathVariable Integer methodId
    ) {
        List<UniversityAdmissionMethodSummaryResponse> result = universityAdmissionMethodService.getSchoolsByMethod(methodId);
        return ApiResponse.<List<UniversityAdmissionMethodSummaryResponse>>builder()
                .code(1000)
                .message("Schools using the method fetched successfully")
                .result(result)
                .build();
    }

    @Operation(summary = "Get all admission methods of a school (latest year only)")
    @GetMapping("/schools/{universityId}")
    public ApiResponse<UniversityAdmissionMethodDetailResponse> getMethodsBySchool(
            @PathVariable Integer universityId
    ) {
        UniversityAdmissionMethodDetailResponse result = universityAdmissionMethodService.getMethodsBySchool(universityId);
        return ApiResponse.<UniversityAdmissionMethodDetailResponse>builder()
                .code(1000)
                .message("Methods of the school fetched successfully")
                .result(result)
                .build();
    }

}
