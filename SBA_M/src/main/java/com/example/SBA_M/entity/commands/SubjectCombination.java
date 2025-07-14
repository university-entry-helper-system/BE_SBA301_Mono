package com.example.SBA_M.entity.commands;

import com.example.SBA_M.utils.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "subject_combinations")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SubjectCombination extends AbstractEntity<Long> {

    @Column(nullable = false, unique = true, length = 10)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @ManyToMany(mappedBy = "subjectCombinations")
    private List<UniversityMajor> majors;

    @ManyToMany
    @JoinTable(
            name = "subject_combination_subjects",
            joinColumns = @JoinColumn(name = "subject_combination_id"),
            inverseJoinColumns = @JoinColumn(name = "exam_subject_id")
    )
    private List<ExamSubject> examSubjects;

    @ManyToOne
    @JoinColumn(name = "block_id")
    private Block block;
}