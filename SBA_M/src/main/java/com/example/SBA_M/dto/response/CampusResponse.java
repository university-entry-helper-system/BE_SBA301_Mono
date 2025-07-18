package com.example.SBA_M.dto.response;

import com.example.SBA_M.utils.Status;
import com.example.SBA_M.dto.response.UniversityResponse;
import com.example.SBA_M.dto.response.ProvinceResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampusResponse {
    private Integer id;
    private String campusName;
    private String campusCode;
    private String address;
    private String phone;
    private String email;
    private String website;
    private Boolean isMainCampus;
    private CampusTypeResponse campusType;
    private String description;
    private Integer establishedYear;
    private BigDecimal areaHectares;
    private UniversityResponse university;
    private ProvinceResponse province;
    private Status status;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
} 