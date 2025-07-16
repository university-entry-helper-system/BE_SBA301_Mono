package com.example.SBA_M.entity.commands;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "campus_types")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CampusType extends AbstractEntity<Integer> {
    @Column(nullable = false, unique = true, length = 255)
    private String name; // Tên loại cơ sở

    @Column(columnDefinition = "TEXT")
    private String description; // Mô tả loại cơ sở
} 