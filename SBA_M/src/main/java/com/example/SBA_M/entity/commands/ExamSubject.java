package com.example.SBA_M.entity.commands;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
}