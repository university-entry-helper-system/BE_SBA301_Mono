package com.example.SBA_M.entity.commands;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "search_count")
@NoArgsConstructor
@AllArgsConstructor
public class SearchCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Khóa chính duy nhất
    @Column(name = "id")
    private Long id;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "university_id", nullable = false)  // Liên kết với University
    private University university;  // Thay vì Integer, dùng đối tượng University


    @Column(name = "search_count", nullable = false)
    private Long searchCount = 0L;  // Số lần tìm kiếm
}