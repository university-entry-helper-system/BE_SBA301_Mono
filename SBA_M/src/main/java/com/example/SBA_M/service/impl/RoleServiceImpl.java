package com.example.SBA_M.service.impl;

import com.example.SBA_M.dto.response.RoleResponse;
import com.example.SBA_M.entity.commands.Role;
import com.example.SBA_M.repository.commands.RoleRepository;
import com.example.SBA_M.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public List<RoleResponse> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    @Override
    public RoleResponse getRoleById(Integer id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + id));
        return toResponse(role);
    }


    @Override
    public RoleResponse saveRole(Role role) {
        Role savedRole = roleRepository.save(role);
        return toResponse(savedRole);
    }

    @Override
    public RoleResponse updateRole(Integer id, Role role) {
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + id));
        existingRole.setName(role.getName());
        existingRole.setDescription(role.getDescription());
        Role updatedRole = roleRepository.save(existingRole);
        return toResponse(updatedRole);
    }

    @Override
    public void deleteRole(Integer id) {
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("Role not found with ID: " + id);
        }
        roleRepository.deleteById(id);
    }
    private RoleResponse toResponse(Role role) {
        RoleResponse response = new RoleResponse();
        response.setId(role.getId());
        response.setName(role.getName().toString());
        return response;
    }
}