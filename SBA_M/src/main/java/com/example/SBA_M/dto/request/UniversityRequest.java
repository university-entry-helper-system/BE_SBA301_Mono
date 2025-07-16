package com.example.SBA_M.dto.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@Data
@RequiredArgsConstructor
public class UniversityRequest {
    private String universityCode; // Mã trường (VD: VNU_HN, HUST, NEU)
    private String nameEn; // Tên tiếng Anh của trường
    @NotNull(message = "Category ID is required")
    private Integer categoryId;
    
    @NotBlank(message = "University name is required")
    private String name;
    private String shortName;
    private MultipartFile logoFile; // File ảnh logo upload từ FE
    private String fanpage; // Link fanpage Facebook
    private Integer foundingYear;
    private String email;
    private String phone;
    private String website;
    private String description;
    private List<Integer> admissionMethodIds;
}
