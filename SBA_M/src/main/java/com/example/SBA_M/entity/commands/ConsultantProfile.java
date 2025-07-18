package com.example.SBA_M.entity.commands;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "consultant_profiles")
@NoArgsConstructor
@AllArgsConstructor
public class ConsultantProfile {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false, insertable = false)
    private UUID id;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "max_concurrent_requests")
    private Integer maxConcurrentRequests = 5;

    @Column(name = "current_pending_requests")
    private Integer currentPendingRequests = 0;

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Account account;

    @ManyToMany
    @JoinTable(
            name = "consultant_specialties",
            joinColumns = @JoinColumn(name = "consultant_id"),
            inverseJoinColumns = @JoinColumn(name = "major_id")
    )
    private List<Major> specialties;

}
