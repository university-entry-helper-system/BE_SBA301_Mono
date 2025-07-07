package com.example.SBA_M.controller;

import com.example.SBA_M.dto.response.ApiResponse;
import com.example.SBA_M.dto.response.RoleResponse;
import com.example.SBA_M.entity.commands.Role;
import com.example.SBA_M.service.impl.RoleServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/roles")
@SecurityRequirement(name = "bearerAuth")
public class RoleController {

    private final RoleServiceImpl roleService;

    public RoleController(RoleServiceImpl roleService) {
        this.roleService = roleService;
    }

    @Operation(summary = "Get all roles")
    @GetMapping
    public ApiResponse<List<RoleResponse>> getAllRoles() {
        List<RoleResponse> roles = roleService.getAllRoles();
        return ApiResponse.<List<RoleResponse>>builder()
                .code(1000)
                .message("Roles fetched successfully")
                .result(roles)
                .build();
    }

    @Operation(summary = "Get role by ID")
    @GetMapping("/{id}")
    public ApiResponse<RoleResponse> getRoleById(@PathVariable Integer id) {
        RoleResponse role = roleService.getRoleById(id);
        return ApiResponse.<RoleResponse>builder()
                .code(1000)
                .message("Role fetched successfully")
                .result(role)
                .build();
    }

    @Operation(summary = "Create a new role")
    @PostMapping
    public ApiResponse<RoleResponse> createRole(@RequestBody Role role) {
        RoleResponse created = roleService.saveRole(role);
        return ApiResponse.<RoleResponse>builder()
                .code(1001)
                .message("Role created successfully")
                .result(created)
                .build();
    }

    @Operation(summary = "Update an existing role")
    @PutMapping("/{id}")
    public ApiResponse<RoleResponse> updateRole(@PathVariable Integer id, @RequestBody Role role) {
        RoleResponse updated = roleService.updateRole(id, role);
        return ApiResponse.<RoleResponse>builder()
                .code(1002)
                .message("Role updated successfully")
                .result(updated)
                .build();
    }

    @Operation(summary = "Delete a role by ID")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteRole(@PathVariable Integer id) {
        roleService.deleteRole(id);
        return ApiResponse.<Void>builder()
                .code(1003)
                .message("Role deleted successfully")
                .build();
    }
}
