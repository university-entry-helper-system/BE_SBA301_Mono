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

    // Tên file ảnh logo lưu trên Minio
    @Column(name = "logo_url", columnDefinition = "TEXT")
    private String logoUrl;

    // Link fanpage Facebook của trường
    @Column(name = "fanpage", length = 255)
    private String fanpage;

    @Column(name = "founding_year")
    private Integer foundingYear;

    @ManyToOne
    @JoinColumn(name = "province_id", nullable = false)
    private Province province;

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
