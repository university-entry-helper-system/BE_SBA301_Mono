package com.example.SBA_M.entity.commands;

import com.example.SBA_M.entity.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "admission_methods")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AdmissionMethod extends AbstractEntity<Long> {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "short_name", length = 50)
    private String shortName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 20)
    private String status = "Active";
}
