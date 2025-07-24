package com.example.SBA_M.dto.response;

import lombok.Data;

@Data
public class SubscriptionPackageResponse {
    private Long id;
    private String name;
    private int price;
    private int totalConsultations;
}
