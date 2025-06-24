package com.example.SBA_M.entity.commands;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "admission_methods")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AdmissionMethod extends AbstractEntity<Integer> {

    @ManyToMany(mappedBy = "admissionMethods")
    private List<UniversityMajor> universityMajors;

    @OneToMany(mappedBy = "admissionMethod", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UniversityAdmissionMethod> admissionMethods;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

}
