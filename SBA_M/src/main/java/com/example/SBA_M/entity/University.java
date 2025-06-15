package com.example.SBA_M.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "universities")
public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column(length = 50)
    private String type;

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

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}