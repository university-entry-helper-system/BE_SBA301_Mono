package com.example.SBA_M.entity.commands;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "consultant_profiles")
@NoArgsConstructor
@AllArgsConstructor
public class ConsultantProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "max_concurrent_requests")
    private Integer maxConcurrentRequests = 5;

    @Column(name = "current_pending_requests")
    private Integer currentPendingRequests = 0;

    @ManyToMany
    @JoinTable(
            name = "consultant_specialties",
            joinColumns = @JoinColumn(name = "consultant_id"),
            inverseJoinColumns = @JoinColumn(name = "major_id")
    )
    private List<Major> specialties;

}
