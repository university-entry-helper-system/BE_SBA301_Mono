package com.example.SBA_M.entity.commands;

import com.example.SBA_M.utils.ConsultationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Entity
@Table(name = "consultations")
@NoArgsConstructor
@AllArgsConstructor
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultant_id")
    private Account consultant;

    @Column(length = 255)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(columnDefinition = "TEXT")
    private String answer;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ConsultationStatus consultationsStatus;

    @Column(name = "sent_at")
    private Instant sentAt;

    @Column(name = "sender_update_at")
    private Instant senderUpdatedAt;

    @Column(name = "consultant_update_at")
    private Instant consultantUpdatedAt;

    @Column(name = "answered_at")
    private Instant answeredAt;

    @Column(name = "resolution_notes", columnDefinition = "TEXT")
    private String resolutionNotes;
}


