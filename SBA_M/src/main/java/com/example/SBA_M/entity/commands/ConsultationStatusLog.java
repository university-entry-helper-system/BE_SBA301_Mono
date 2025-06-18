package com.example.SBA_M.entity.commands;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "consultation_status_logs")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ConsultationStatusLog extends AbstractEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "consultation_id", nullable = false)
    private Consultation consultation;

    @Column(length = 20)
    private String logStatus;

    @Column(name = "changed_by")
    private UUID changedBy;

    @Column(name = "changed_at")
    private Instant changedAt;

    @Column(columnDefinition = "TEXT")
    private String notes;
}