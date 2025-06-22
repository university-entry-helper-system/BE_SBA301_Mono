package com.example.SBA_M.entity.commands;

import com.example.SBA_M.utils.AccountStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Table(name = "universities")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class University extends AbstractEntity<Integer> {
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private UniversityCategory category;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "short_name", length = 50)
    private String shortName;

    @Column(name = "logo_url", length = 255)
    private String logoUrl;

    @Column(name = "founding_year")
    private Integer foundingYear;

    @Column(length = 100)
    private String province;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(length = 255)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 255)
    private String website;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<News> news;

    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UniversityMajor> universityMajors;

    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UniversityAdmissionMethod> admissionMethods;
}
