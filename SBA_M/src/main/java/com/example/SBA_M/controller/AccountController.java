package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.*;
import com.example.SBA_M.dto.response.AccountResponse;
import com.example.SBA_M.dto.response.ApiResponse;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/accounts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@SecurityRequirement(name = "bearerAuth")
public class AccountController {

    AccountService accountService;

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create general user")
    @PostMapping("/create-user")
    public ApiResponse<AccountResponse> createGeneralUser(@RequestBody @Valid AccountCreationRequest request) {
        return ApiResponse.<AccountResponse>builder()
                .code(1001)
                .message("General user created successfully")
                .result(accountService.createGeneralUser(request))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create admin user")
    @PostMapping("/create-admin")
    public ApiResponse<AccountResponse> createAdminUser(@RequestBody @Valid AccountCreationRequest request) {
        return ApiResponse.<AccountResponse>builder()
                .code(1001)
                .message("Admin user created successfully")
                .result(accountService.createAdminUser(request))
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CONSULTANT')")
    @Operation(summary = "Get all users")
    @GetMapping
    public ApiResponse<PageResponse<AccountResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<AccountResponse>>builder()
                .code(1000)
                .message("Users fetched successfully")
                .result(accountService.getAllUsers(page, size))
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CONSULTANT')")
    @Operation(summary = "Search users by name")
    @GetMapping("/search")
    public ApiResponse<PageResponse<AccountResponse>> searchUsers(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<AccountResponse>>builder()
                .code(1000)
                .message("Users fetched successfully")
                .result(accountService.getUsersBySearch(name, page, size))
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CONSULTANT') or @accountServiceImpl.isAccountOwner(#accountId)")
    @Operation(summary = "Get user by ID")
    @GetMapping("/{accountId}")
    public ApiResponse<AccountResponse> getUserById(@PathVariable UUID accountId) {
        return ApiResponse.<AccountResponse>builder()
                .code(1000)
                .message("User fetched successfully")
                .result(accountService.getUserById(accountId))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete user")
    @DeleteMapping("/{accountId}")
    public ApiResponse<Void> deleteUser(@PathVariable UUID accountId,
                                        @RequestParam(value = "hard", defaultValue = "false") boolean hardDelete) {
        accountService.deleteUser(accountId, hardDelete);
        return ApiResponse.<Void>builder()
                .code(1003)
                .message("User deleted successfully")
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Restore user")
    @PostMapping("/{accountId}/restore")
    public ApiResponse<Void> restoreUser(@PathVariable UUID accountId) {
        accountService.restoreUser(accountId);
        return ApiResponse.<Void>builder()
                .code(1002)
                .message("User restored successfully")
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update user info")
    @PutMapping("/{accountId}")
    public ApiResponse<AccountResponse> updateUser(@PathVariable UUID accountId,
                                                   @RequestBody @Valid UserUpdateRequest request) {
        return ApiResponse.<AccountResponse>builder()
                .code(1002)
                .message("User updated successfully")
                .result(accountService.updateUser(accountId, request))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Assign roles")
    @PostMapping("/{accountId}/set-roles")
    public ApiResponse<AccountResponse> setRoles(@PathVariable UUID accountId,
                                                 @RequestBody @Valid RoleAssignmentRequest request) {
        return ApiResponse.<AccountResponse>builder()
                .code(1002)
                .message("Roles set successfully")
                .result(accountService.setRoles(accountId, new HashSet<>(request.getRoleIds())))
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CONSULTANT') or @accountServiceImpl.isAccountOwner(#accountId)")
    @Operation(summary = "Get user roles")
    @GetMapping("/{accountId}/roles")
    public ApiResponse<Set<String>> getUserRoles(@PathVariable UUID accountId) {
        return ApiResponse.<Set<String>>builder()
                .code(1000)
                .message("Roles fetched successfully")
                .result(accountService.getUserRoles(accountId))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update user status")
    @PatchMapping("/{accountId}/status")
    public ApiResponse<AccountResponse> updateUserStatus(@PathVariable UUID accountId,
                                                         @RequestBody @Valid AccountStatusUpdateRequest request) {
        return ApiResponse.<AccountResponse>builder()
                .code(1002)
                .message("User status updated successfully")
                .result(accountService.updateStatus(accountId, request.getStatus()))
                .build();
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get current user info")
    @GetMapping("/my-info")
    public ApiResponse<AccountResponse> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return ApiResponse.<AccountResponse>builder()
                .code(1000)
                .message("Current user info fetched")
                .result(accountService.getMyInfo(userDetails.getUsername()))
                .build();
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update password")
    @PostMapping("/my-info/update-password")
    public ApiResponse<Void> updateMyPassword(@AuthenticationPrincipal UserDetails userDetails,
                                              @RequestBody @Valid UpdatePasswordRequest request) {
        accountService.updatePassword(userDetails.getUsername(), request);
        return ApiResponse.<Void>builder()
                .code(1002)
                .message("Password updated successfully")
                .build();
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update profile")
    @PutMapping("/my-info/update-profile")
    public ApiResponse<AccountResponse> updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                                      @RequestBody @Valid UserUpdateRequest request) {
        return ApiResponse.<AccountResponse>builder()
                .code(1002)
                .message("Profile updated successfully")
                .result(accountService.updateProfile(userDetails.getUsername(), request))
                .build();
    }
}

