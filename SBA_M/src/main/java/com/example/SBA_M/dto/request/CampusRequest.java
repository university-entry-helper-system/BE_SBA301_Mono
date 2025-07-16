package com.example.SBA_M.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CampusRequest {
    private Integer universityId;
    private Integer provinceId;
    private String campusName;
    private String campusCode;
    private String address;
    private String phone;
    private String email;
    private String website;
    private Boolean isMainCampus = false;
    private Integer campusTypeId; // ID của loại cơ sở
    private String description;
    private Integer establishedYear;
    private BigDecimal areaHectares;
} 