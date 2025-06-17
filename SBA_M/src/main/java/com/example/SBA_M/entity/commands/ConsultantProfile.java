package com.example.SBA_M.entity.commands;

import com.example.SBA_M.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "consultant_profiles")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ConsultantProfile extends AbstractEntity<UUID> {

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "max_concurrent_requests")
    private Integer maxConcurrentRequests = 5;

    @Column(name = "current_pending_requests")
    private Integer currentPendingRequests = 0;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Account account;
}
