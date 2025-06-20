package com.example.SBA_M.entity.commands;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "university_majors", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"university_id", "major_id", "year"})
})
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UniversityMajor extends AbstractEntity<Integer> {

    @ManyToOne(optional = false)
    @JoinColumn(name = "university_id", nullable = false)
    private University university;

    @ManyToOne(optional = false)
    @JoinColumn(name = "major_id", nullable = false)
    private Major major;

    @ManyToMany
    @JoinTable(
            name = "university_major_admission_methods",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "admission_method_id")
    )
    private List<AdmissionMethod> admissionMethods;


    @Column(nullable = false)
    private Integer year;

    @Column(precision = 18, scale = 2)
    private BigDecimal tuition;

    @Column
    private Integer quota;

    @Column(columnDefinition = "TEXT")
    private String notes;
}
