package com.example.SBA_M.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "admission_benchmarks", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "university_id", "major_id", "year", "admission_method_id", "subject_combination_id"
        })
})
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AdmissionBenchmark extends AbstractEntity<Long> {

    @ManyToOne(optional = false)
    @JoinColumn(name = "university_id")
    private University university;

    @ManyToOne(optional = false)
    @JoinColumn(name = "major_id")
    private Major major;

    @Column(nullable = false)
    private Integer year;

    @ManyToOne(optional = false)
    @JoinColumn(name = "admission_method_id")
    private AdmissionMethod admissionMethod;

    @ManyToOne
    @JoinColumn(name = "subject_combination_id")
    private SubjectCombination subjectCombination;

    @Column(name = "base_score")
    private Float baseScore;

    @Column(name = "priority_points")
    private Float priorityPoints = 0.0f;

    @Column(name = "total_score", nullable = false)
    private Float totalScore;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "specific_method_notes", columnDefinition = "TEXT")
    private String specificMethodNotes;
}
