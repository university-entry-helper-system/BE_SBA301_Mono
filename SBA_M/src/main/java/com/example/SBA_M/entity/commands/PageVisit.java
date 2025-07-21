
package com.example.SBA_M.entity.commands;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;


@Data
@Entity
@Table(name = "page_visits")
@NoArgsConstructor
@AllArgsConstructor
public class PageVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "count", nullable = false)
    private Long count = 0L; // Số lần truy cập, mặc định là 0

   @Column(name = "created_at", nullable = false, updatable = false)
   private LocalDate createdAt = LocalDate.now();

}
