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

    @Column(name = "major_group", length = 100)
    private String majorGroup;

    @Column(length = 100)
    private String degree;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "major", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UniversityMajor> universityMajors;
}