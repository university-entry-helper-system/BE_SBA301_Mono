package com.example.SBA_M.event;

import com.example.SBA_M.utils.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UniversityMajorSearchEvent {
    private String id; // Composite: universityId-majorId-subjectCombinationId
    private Integer universityId;
    private String universityName;
    private Long majorId;
    private String majorName;
    private Long subjectCombinationId;
    private String subjectCombinationName;
    private Integer universityMajorCountByMajor;
    private Integer universityMajorCountBySubjectCombination;
    private Integer year;
    private Status status;
}

