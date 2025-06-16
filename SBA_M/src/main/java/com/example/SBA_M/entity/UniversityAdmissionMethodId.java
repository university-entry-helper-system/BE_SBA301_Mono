package com.example.SBA_M.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class UniversityAdmissionMethodId implements Serializable {
    private Long universityId;
    private Long admissionMethodId;
    private Integer year;
}