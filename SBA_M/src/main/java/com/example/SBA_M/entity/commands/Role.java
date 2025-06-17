package com.example.SBA_M.entity.commands;

import com.example.SBA_M.entity.AbstractEntity;
import com.example.SBA_M.utils.RoleName;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "accounts")
public class Role extends AbstractEntity<Long> {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 50)
    private RoleName name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToMany(mappedBy = "roles")
    private Set<Account> accounts;
}
