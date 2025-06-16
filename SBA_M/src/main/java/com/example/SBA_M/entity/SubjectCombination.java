package com.example.SBA_M.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
}