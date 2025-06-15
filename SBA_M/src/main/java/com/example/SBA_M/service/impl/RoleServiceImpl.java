package com.example.SBA_M.service.impl;

import com.example.SBA_M.entity.Role;
import com.example.SBA_M.repository.RoleRepository;
import com.example.SBA_M.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + id));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Role saveRole(Role role) {
        // Có thể thêm logic kiểm tra trùng tên role tại đây
        return roleRepository.save(role);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Role updateRole(Long id, Role role) {
        Role existingRole = getRoleById(id); // Sử dụng phương thức getRoleById đã có quyền
        existingRole.setName(role.getName());
        existingRole.setDescription(role.getDescription());
        return roleRepository.save(existingRole);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("Role not found with ID: " + id);
        }
        roleRepository.deleteById(id);
    }
}