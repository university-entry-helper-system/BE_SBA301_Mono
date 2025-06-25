package com.example.SBA_M.dto.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class UniversityRequest {
    private Integer categoryId;
    private String name;
    private String shortName;
    private String logoUrl;
    private Integer foundingYear;
    private Integer provinceId;
    private String type;
    private String address;
    private String email;
    private String phone;
    private String website;
    private String description;
    private List<Integer> admissionMethodIds;
}
