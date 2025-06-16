package com.example.SBA_M.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "university_admission_methods")
@NoArgsConstructor
@AllArgsConstructor
public class UniversityAdmissionMethod {

    @EmbeddedId
    private UniversityAdmissionMethodId id;

    @ManyToOne
    @MapsId("universityId")
    @JoinColumn(name = "university_id")
    private University university;

    @ManyToOne
    @MapsId("admissionMethodId")
    @JoinColumn(name = "admission_method_id")
    private AdmissionMethod admissionMethod;

    @Column(name = "quota_percentage")
    private Float quotaPercentage;

    @Column(columnDefinition = "TEXT")
    private String notes;
}
