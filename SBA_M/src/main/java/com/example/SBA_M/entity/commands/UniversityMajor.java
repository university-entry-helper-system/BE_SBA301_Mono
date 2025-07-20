package com.example.SBA_M.entity.commands;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "university_majors")

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UniversityMajor extends AbstractEntity<Integer> {

    @ManyToOne(optional = false)
    @JoinColumn(name = "university_id",unique = false, nullable = false)
    private University university;

    @ManyToOne(optional = false)
    @JoinColumn(name = "major_id",unique = false, nullable = false)
    private Major major;

    @ManyToMany
    @JoinTable(
            name = "university_major_admission_methods",
            joinColumns = @JoinColumn(name = "university_major_id"),
            inverseJoinColumns = @JoinColumn(name = "admission_method_id")
    )
    private List<AdmissionMethod> admissionMethods;

    @Column(name = "university_major_name", nullable = false, length = 255)
    private String universityMajorName;


    @Column
    private Double score;

    @Column
    private Integer quota;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "year", nullable = false)
    private Integer year; // Năm tuyển sinh

    @ManyToMany
    @JoinTable(
            name = "major_subject_combinations",
            joinColumns = @JoinColumn(name = "university_major_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_combination_id")
    )
    private List<SubjectCombination> subjectCombinations;
}
