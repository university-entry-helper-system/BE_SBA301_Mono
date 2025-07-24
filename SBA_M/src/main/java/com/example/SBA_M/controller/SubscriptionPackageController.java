package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.SubscriptionPackageRequest;
import com.example.SBA_M.dto.response.SubscriptionPackageResponse;
import com.example.SBA_M.service.SubscriptionPackageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
@RequiredArgsConstructor
@Tag(name = "Subscription Package Controller", description = "APIs for managing subscription packages")
public class SubscriptionPackageController {
    private final SubscriptionPackageService packageService;

    @GetMapping
    public List<SubscriptionPackageResponse> getAllPackages() {
        return packageService.getAllPackages();
    }

    @GetMapping("/{id}")
    public SubscriptionPackageResponse getPackageById(@PathVariable Long id) {
        return packageService.getPackageById(id);
    }

    @PostMapping
    public SubscriptionPackageResponse createPackage(@RequestBody SubscriptionPackageRequest request) {
        return packageService.createPackage(request);
    }

    @PutMapping("/{id}")
    public SubscriptionPackageResponse updatePackage(@PathVariable Long id, @RequestBody SubscriptionPackageRequest request) {
        return packageService.updatePackage(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePackage(@PathVariable Long id) {
        packageService.deletePackage(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/buy/{packageId}")
    public ResponseEntity<?> buyPackage(@PathVariable Long packageId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        packageService.buyPackage(username, packageId);
        return ResponseEntity.ok().build();
    }
} 