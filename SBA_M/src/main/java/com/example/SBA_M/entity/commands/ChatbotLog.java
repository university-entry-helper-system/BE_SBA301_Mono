package com.example.SBA_M.entity.commands;

import com.example.SBA_M.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "chatbot_logs")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ChatbotLog extends AbstractEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String question;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String answer;

    @Column
    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @Column(name = "model_used", length = 100)
    private String modelUsed;

    @Column(name = "intent_detected", length = 100)
    private String intentDetected;
}