package com.example.SBA_M.service;

import com.example.SBA_M.entity.commands.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();
    Role getRoleById(Integer id);
    Role saveRole(Role role);
    Role updateRole(Integer id, Role role);
    void deleteRole(Integer id);
}