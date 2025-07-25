package com.example.SBA_M.dto.response;

import com.example.SBA_M.entity.commands.Province;
import com.example.SBA_M.utils.Status;
import com.example.SBA_M.dto.response.UniversityCategoryResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UniversityResponse {
    private Integer id;
    private String universityCode; // Mã trường (VD: VNU_HN, HUST, NEU)
    private String nameEn; // Tên tiếng Anh của trường
    private Integer categoryId;
    private UniversityCategoryResponse category;
    private String name;
    private String shortName;
    private String logoUrl; // URL public ảnh logo trả về cho FE
    private String fanpage; // Link fanpage Facebook
    private Integer foundingYear;
    private String email;
    private String phone;
    private String website;
    private String description;
    private Status status;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private List<Integer> admissionMethodIds;
    private Integer campusCount; // Số lượng cơ sở
    private List<CampusResponse> campuses; // Danh sách cơ sở (optional)
}