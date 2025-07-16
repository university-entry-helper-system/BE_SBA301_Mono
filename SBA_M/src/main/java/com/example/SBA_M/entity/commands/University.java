package com.example.SBA_M.entity.commands;

import com.example.SBA_M.utils.AccountStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "universities")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class University extends AbstractEntity<Integer> {
    
    @Column(name = "university_code", unique = true, length = 20, nullable = true)
    private String universityCode; // Mã trường (VD: VNU_HN, HUST, NEU)

    @Column(name = "name_en")
    private String nameEn; // Tên tiếng Anh của trường

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private UniversityCategory category;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "short_name", length = 50)
    private String shortName;

    // Tên file ảnh logo lưu trên Minio
    @Column(name = "logo_url", columnDefinition = "TEXT")
    private String logoUrl;

    // Link fanpage Facebook của trường
    @Column(name = "fanpage", length = 255)
    private String fanpage;

    @Column(name = "founding_year", nullable = true)
    private Integer foundingYear;

    @Column(length = 255, nullable = true)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 255)
    private String website;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Campus> campuses = new ArrayList<>(); // Danh sách cơ sở

    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<News> news;

    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UniversityMajor> universityMajors;

    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<UniversityAdmissionMethod> admissionMethods;
}
