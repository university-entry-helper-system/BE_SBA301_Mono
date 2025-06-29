package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.AdmissionMethodRequest;
import com.example.SBA_M.dto.response.AdmissionMethodResponse;
import com.example.SBA_M.dto.response.ApiResponse;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.service.AdmissionMethodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/admission-methods")
@SecurityRequirement(name = "bearerAuth")
public class AdmissionMethodController {

    private final AdmissionMethodService admissionMethodService;

    public AdmissionMethodController(AdmissionMethodService admissionMethodService) {
        this.admissionMethodService = admissionMethodService;
    }

    @Operation(summary = "Get all admission methods (paginated)")
    @GetMapping
    public ApiResponse<PageResponse<AdmissionMethodResponse>> getAllAdmissionMethods(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<AdmissionMethodResponse> response = admissionMethodService.getAllAdmissionMethods(page, size);
        return ApiResponse.<PageResponse<AdmissionMethodResponse>>builder()
                .code(1000)
                .message("Fetched all admission methods")
                .result(response)
                .build();
    }

    @Operation(summary = "Get admission method by ID")
    @GetMapping("/{id}")
    public ApiResponse<AdmissionMethodResponse> getAdmissionMethodById(@PathVariable Integer id) {
        AdmissionMethodResponse response = admissionMethodService.getAdmissionMethodById(id);
        return ApiResponse.<AdmissionMethodResponse>builder()
                .code(1000)
                .message("Admission method fetched successfully")
                .result(response)
                .build();
    }

    @Operation(summary = "Create a new admission method")
    @PostMapping
    public ApiResponse<AdmissionMethodResponse> createAdmissionMethod(@RequestBody AdmissionMethodRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AdmissionMethodResponse created = admissionMethodService.createAdmissionMethod(request, auth.getName());
        return ApiResponse.<AdmissionMethodResponse>builder()
                .code(1001)
                .message("Admission method created successfully")
                .result(created)
                .build();
    }

    @Operation(summary = "Update an admission method")
    @PutMapping("/{id}")
    public ApiResponse<AdmissionMethodResponse> updateAdmissionMethod(
            @PathVariable Integer id,
            @RequestBody AdmissionMethodRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AdmissionMethodResponse updated = admissionMethodService.updateAdmissionMethod(id, request, auth.getName());
        return ApiResponse.<AdmissionMethodResponse>builder()
                .code(1002)
                .message("Admission method updated successfully")
                .result(updated)
                .build();
    }

    @Operation(summary = "Delete an admission method (soft delete)")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteAdmissionMethod(@PathVariable Integer id) {
        admissionMethodService.deleteAdmissionMethod(id);
        return ApiResponse.<Void>builder()
                .code(1003)
                .message("Admission method deleted successfully")
                .build();
    }
}
