package com.example.SBA_M.entity.commands;

import com.example.SBA_M.utils.StatusConsultant;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusConsultant status = StatusConsultant.OFFLINE;

    @ManyToMany
    @JoinTable(
            name = "consultant_specialties",
            joinColumns = @JoinColumn(name = "consultant_id"),
            inverseJoinColumns = @JoinColumn(name = "major_id")
    )
    private List<Major> specialties;

}
