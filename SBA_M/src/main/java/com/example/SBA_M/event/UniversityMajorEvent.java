package com.example.SBA_M.event;

import com.example.SBA_M.utils.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UniversityMajorEvent {
    private String id;

    private Integer universityId;
    private String universityName;

    private Long majorId;
    private String majorName;

    private Integer methodId;
    private String methodName;

    private Long subjectCombinationId;
    private String subjectCombination;

    private Double score;
    private String note;
    private Status status;
}

