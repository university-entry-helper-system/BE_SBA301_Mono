package com.example.SBA_M.entity.commands;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "universities")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Province extends AbstractEntity<Integer>{
    private String name;
    private String region; // e.g., "North", "South", "East", "West"
}
