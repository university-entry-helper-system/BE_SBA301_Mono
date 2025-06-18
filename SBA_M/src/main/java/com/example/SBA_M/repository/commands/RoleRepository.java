package com.example.SBA_M.repository.commands;

import com.example.SBA_M.entity.commands.Role;
import com.example.SBA_M.utils.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(RoleName name);

    List<Role> findAllByIdIn(List<Integer> ids);
}