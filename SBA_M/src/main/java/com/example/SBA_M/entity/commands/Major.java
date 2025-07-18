package com.example.SBA_M.entity.commands;

import com.example.SBA_M.utils.Status;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @OneToMany(mappedBy = "major", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UniversityMajor> universityMajors;

    @ManyToMany(mappedBy = "specialties")
    private List<ConsultantProfile> consultants;

}