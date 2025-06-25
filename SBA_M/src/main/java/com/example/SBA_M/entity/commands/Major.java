package com.example.SBA_M.entity.commands;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Table(name = "majors")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Major extends AbstractEntity<Long> {

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 20, unique = true)
    private String code;

    @Column(length = 100)
    private String degree;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "major", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UniversityMajor> universityMajors;

    @ManyToMany(mappedBy = "specialties")
    private List<ConsultantProfile> consultants;

    @ManyToMany
    @JoinTable(
            name = "major_subject_combinations",
            joinColumns = @JoinColumn(name = "major_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_combination_id")
    )
    private List<SubjectCombination> subjectCombinations;
}