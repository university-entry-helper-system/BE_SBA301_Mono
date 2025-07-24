package com.example.SBA_M.entity.commands;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "scores")
@NoArgsConstructor
@AllArgsConstructor
public class Score extends AbstractEntity<Long> {

    @Column(nullable = false, length = 100)
    private String subject;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false, length = 255)
    private String scoreUrl;

    @Column(nullable = false, length = 50)
    private String type;
}