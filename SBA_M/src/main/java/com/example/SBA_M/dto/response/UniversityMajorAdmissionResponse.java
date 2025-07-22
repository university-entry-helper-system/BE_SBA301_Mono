package com.example.SBA_M.dto.response;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UniversityMajorAdmissionResponse {
    private Integer universityId;
    private String universityName;
    private Long majorId;
    private String majorName;
    private String uniMajorName;
    private Double score;
    private Integer year;
}