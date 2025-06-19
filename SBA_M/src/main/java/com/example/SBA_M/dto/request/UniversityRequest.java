package com.example.SBA_M.dto.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UniversityRequest {
    private Integer id;
    private Integer categoryId;
    private String name;
    private String shortName;
    private String logoUrl;
    private Integer foundingYear;
    private String province;
    private String type;
    private String address;
    private String email;
    private String phone;
    private String website;
    private String description;

}
