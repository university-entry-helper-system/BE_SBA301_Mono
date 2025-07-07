package com.example.SBA_M.service;

import com.example.SBA_M.dto.response.RoleResponse;
import com.example.SBA_M.entity.commands.Role;

import java.util.List;

public interface RoleService {
    List<RoleResponse> getAllRoles();
    RoleResponse getRoleById(Integer id);
    RoleResponse saveRole(Role role);
    RoleResponse updateRole(Integer id, Role role);
    void deleteRole(Integer id);
}