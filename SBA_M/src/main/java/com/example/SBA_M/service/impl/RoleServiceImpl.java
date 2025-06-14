package com.example.SBA_M.service.impl;
import com.example.SBA_M.entity.Role;

import java.util.List;

public interface RoleServiceImpl {
    List<Role> getAllRoles();
    Role getRoleById(Long id);
    Role saveRole(Role role);
    Role updateRole(Long id, Role role);
    void deleteRole(Long id);
}