package com.example.SBA_M.dto.request;

import lombok.Data;

@Data

public class SubscriptionPackageRequest {
    private String name;
    private int price;
    private int totalConsultations;
}
