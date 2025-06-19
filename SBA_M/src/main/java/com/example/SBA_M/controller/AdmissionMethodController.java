package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.AdmissionMethodRequest;
import com.example.SBA_M.dto.response.AdmissionMethodResponse;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.service.AdmissionMethodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/admission-methods" )
@SecurityRequirement(name = "bearerAuth")
public class AdmissionMethodController {
    private final AdmissionMethodService admissionMethodService;

    public AdmissionMethodController(AdmissionMethodService admissionMethodService) {
        this.admissionMethodService = admissionMethodService;
    }

    @Operation(summary = "Get all admission methods (paginated)")
    @GetMapping
    public ResponseEntity<PageResponse<AdmissionMethodResponse>> getAllAdmissionMethods(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<AdmissionMethodResponse> response = admissionMethodService.getAllAdmissionMethods(page, size);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get admission method by ID")
    @GetMapping("/{id}")
    public ResponseEntity<AdmissionMethodResponse> getAdmissionMethodById(@PathVariable Integer id) {
        AdmissionMethodResponse response = admissionMethodService.getAdmissionMethodById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create a new admission method")
    @PostMapping
    public ResponseEntity<AdmissionMethodResponse> createAdmissionMethod(@RequestBody AdmissionMethodRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AdmissionMethodResponse created = admissionMethodService.createAdmissionMethod(request, auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Update an admission method")
    @PutMapping("/{id}")
    public ResponseEntity<AdmissionMethodResponse> updateAdmissionMethod(
            @PathVariable Integer id,
            @RequestBody AdmissionMethodRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AdmissionMethodResponse updated = admissionMethodService.updateAdmissionMethod(id, request, auth.getName());
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete an admission method (soft delete)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmissionMethod(@PathVariable Integer id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        admissionMethodService.deleteAdmissionMethod(id);
        return ResponseEntity.noContent().build();
    }
}