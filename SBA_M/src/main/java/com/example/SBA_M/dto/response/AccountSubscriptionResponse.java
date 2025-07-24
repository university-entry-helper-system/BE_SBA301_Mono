package com.example.SBA_M.dto.response;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class AccountSubscriptionResponse {
    private UUID accountId;
    private List<SubscriptionPackageResponse> subscriptionPackages;
}