package com.example.SBA_M.entity.commands;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ConsultantSpecialtyId implements Serializable {
    private UUID consultantId;
    private Long majorId;
}
