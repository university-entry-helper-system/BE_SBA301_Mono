package com.example.SBA_M.entity.commands;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "campuses")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Campus extends AbstractEntity<Integer> {
    
    @Column(name = "campus_name", nullable = false, length = 255)
    private String campusName; // Tên cơ sở

    @Column(name = "campus_code", length = 50)
    private String campusCode; // Mã cơ sở trong trường (VD: "MAIN", "CS1")

    @Column(name = "address", nullable = false, columnDefinition = "TEXT")
    private String address; // Địa chỉ chi tiết

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "website")
    private String website;

    @Column(name = "is_main_campus")
    private Boolean isMainCampus = false; // Cơ sở chính

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campus_type_id", nullable = false)
    private CampusType campusType;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "established_year")
    private Integer establishedYear;

    @Column(name = "area_hectares", precision = 10, scale = 2)
    private BigDecimal areaHectares;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id", nullable = false)
    private University university;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id", nullable = false)
    private Province province;
} 