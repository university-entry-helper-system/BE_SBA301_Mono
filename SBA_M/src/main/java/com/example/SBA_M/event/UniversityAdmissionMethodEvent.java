package com.example.SBA_M.event;

import com.example.SBA_M.utils.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UniversityAdmissionMethodEvent {
    private Integer id;
    private Integer universityId;
    private String universityName;



    private Integer methodId;
    private String methodName;

    private Integer year;
    private String notes;
    private String conditions;
    private String regulations;
    private String admissionTime;

    private Status status;
}
