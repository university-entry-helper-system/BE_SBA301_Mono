package com.example.SBA_M.entity.commands;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "university_admission_methods_documents")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UniversityAdmissionMethod extends AbstractEntity<Integer>{

    @ManyToOne
    @JoinColumn(name = "university_id")
    private University university;

    @ManyToOne
    @JoinColumn(name = "admission_method_id")
    private AdmissionMethod admissionMethod;

    @Column(nullable = false)
    private Integer year;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(columnDefinition = "TEXT")
    private String conditions;

    @Column(columnDefinition = "TEXT")
    private String regulations;

    @Column(name="admission_time" ,columnDefinition = "TEXT")
    private String admissionTime;
}
