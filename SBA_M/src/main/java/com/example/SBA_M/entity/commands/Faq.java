package com.example.SBA_M.entity.commands;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "faqs")
@NoArgsConstructor
@AllArgsConstructor
public class Faq {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String question;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String answer;
}