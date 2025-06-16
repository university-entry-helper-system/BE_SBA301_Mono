package com.example.SBA_M.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "consultant_specialties")
@NoArgsConstructor
@AllArgsConstructor
public class ConsultantSpecialty {

    @EmbeddedId
    private ConsultantSpecialtyId id;

    @ManyToOne
    @MapsId("consultantId")
    @JoinColumn(name = "consultant_id")
    private ConsultantProfile consultant;

    @ManyToOne
    @MapsId("majorId")
    @JoinColumn(name = "major_id")
    private Major major;
}