package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.SubscriptionPackageRequest;
import com.example.SBA_M.dto.response.SubscriptionPackageResponse;
import com.example.SBA_M.entity.commands.SubscriptionPackage;
import java.util.List;

public interface SubscriptionPackageService {
    SubscriptionPackageResponse createPackage(SubscriptionPackageRequest request);
    SubscriptionPackageResponse updatePackage(Long id, SubscriptionPackageRequest request);
    void deletePackage(Long id);
    List<SubscriptionPackageResponse> getAllPackages();
    SubscriptionPackageResponse getPackageById(Long id);
    void buyPackage(String username, Long packageId);
} 