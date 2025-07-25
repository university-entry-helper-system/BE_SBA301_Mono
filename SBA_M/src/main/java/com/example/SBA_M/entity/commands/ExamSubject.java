package com.example.SBA_M.entity.commands;

import com.example.SBA_M.utils.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "exam_subjects")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ExamSubject extends AbstractEntity<Long> {

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "short_name", length = 20)
    private String shortName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @ManyToMany(mappedBy = "examSubjects")
    private List<SubjectCombination> subjectCombinations;
}