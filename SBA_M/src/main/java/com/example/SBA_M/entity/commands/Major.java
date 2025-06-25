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

    @OneToMany(mappedBy = "major", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UniversityMajor> universityMajors;

    @ManyToMany(mappedBy = "specialties")
    private List<ConsultantProfile> consultants;

}