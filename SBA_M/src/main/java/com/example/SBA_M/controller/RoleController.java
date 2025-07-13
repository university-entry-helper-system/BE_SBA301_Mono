package com.example.SBA_M.controller;

import com.example.SBA_M.dto.response.ApiResponse;
import com.example.SBA_M.dto.response.RoleResponse;
import com.example.SBA_M.entity.commands.Role;
import com.example.SBA_M.service.RoleService;
import com.example.SBA_M.service.impl.RoleServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/roles")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "Get all roles")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ApiResponse<List<RoleResponse>> getAllRoles() {
        return ApiResponse.<List<RoleResponse>>builder()
                .code(1000)
                .message("Roles fetched successfully")
                .result(roleService.getAllRoles())
                .build();
    }

    @Operation(summary = "Get role by ID")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ApiResponse<RoleResponse> getRoleById(@PathVariable Integer id) {
        return ApiResponse.<RoleResponse>builder()
                .code(1000)
                .message("Role fetched successfully")
                .result(roleService.getRoleById(id))
                .build();
    }

    @Operation(summary = "Create a new role")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ApiResponse<RoleResponse> createRole(@Valid @RequestBody Role role) {
        return ApiResponse.<RoleResponse>builder()
                .code(1001)
                .message("Role created successfully")
                .result(roleService.saveRole(role))
                .build();
    }

    @Operation(summary = "Update an existing role")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ApiResponse<RoleResponse> updateRole(@PathVariable Integer id, @Valid @RequestBody Role role) {
        return ApiResponse.<RoleResponse>builder()
                .code(1002)
                .message("Role updated successfully")
                .result(roleService.updateRole(id, role))
                .build();
    }

    @Operation(summary = "Delete a role by ID")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteRole(@PathVariable Integer id) {
        roleService.deleteRole(id);
        return ApiResponse.<Void>builder()
                .code(1003)
                .message("Role deleted successfully")
                .build();
    }
}

