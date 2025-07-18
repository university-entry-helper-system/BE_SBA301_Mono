package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.*;
import com.example.SBA_M.dto.response.AccountResponse;
import com.example.SBA_M.dto.response.ApiResponse;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.dto.response.BulkActionResponse;
import com.example.SBA_M.dto.response.ActivityLogResponse;
import com.example.SBA_M.dto.response.LoginHistoryResponse;
import com.example.SBA_M.dto.response.PasswordStrengthResponse;
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
import com.example.SBA_M.dto.response.AccountStatsResponse;

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
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false) String sort) {
        return ApiResponse.<PageResponse<AccountResponse>>builder()
                .code(1000)
                .message("Users fetched successfully")
                .result(accountService.getAllUsers(page, size, sort))
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
    @Operation(summary = "Promote/Demote user role (USER ↔ CONSULTANT)")
    @PostMapping("/{accountId}/set-roles")
    public ApiResponse<AccountResponse> setRoles(@PathVariable UUID accountId,
                                                 @RequestBody @Valid RoleChangeRequest request) {
        return ApiResponse.<AccountResponse>builder()
                .code(1002)
                .message("Role changed successfully")
                .result(accountService.changeUserRole(accountId, request.getAction(), request.getReason()))
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

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Ban/Unban user")
    @PatchMapping("/{accountId}/ban")
    public ApiResponse<AccountResponse> banUser(@PathVariable UUID accountId,
                                               @RequestBody @Valid BanUserRequest request) {
        return ApiResponse.<AccountResponse>builder()
                .code(1004)
                .message(request.isBanned() ? "User banned successfully" : "User unbanned successfully")
                .result(accountService.banUser(accountId, request.isBanned()))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Reset user password (ADMIN)")
    @PostMapping("/{accountId}/reset-password")
    public ApiResponse<Void> resetUserPassword(@PathVariable UUID accountId,
                                              @RequestBody @Valid ResetPasswordRequest request) {
        accountService.resetUserPassword(accountId, request.getNewPassword());
        return ApiResponse.<Void>builder()
                .code(1005)
                .message("Password reset email sent successfully")
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Bulk actions")
    @PostMapping("/bulk-action")
    public ApiResponse<BulkActionResponse> bulkAction(@RequestBody @Valid BulkActionRequest request) {
        return ApiResponse.<BulkActionResponse>builder()
                .code(1007)
                .message("Bulk action executed successfully")
                .result(accountService.executeBulkAction(request))
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CONSULTANT')")
    @Operation(summary = "Get user activity log")
    @GetMapping("/{accountId}/activity-log")
    public ApiResponse<PageResponse<ActivityLogResponse>> getActivityLog(@PathVariable UUID accountId,
                                                                        @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<ActivityLogResponse>>builder()
                .code(1009)
                .message("Activity log fetched successfully")
                .result(accountService.getActivityLog(accountId, page, size))
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CONSULTANT')")
    @Operation(summary = "Get user login history")
    @GetMapping("/{accountId}/login-history")
    public ApiResponse<PageResponse<LoginHistoryResponse>> getLoginHistory(@PathVariable UUID accountId,
                                                                          @RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<LoginHistoryResponse>>builder()
                .code(1010)
                .message("Login history fetched successfully")
                .result(accountService.getLoginHistory(accountId, page, size))
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CONSULTANT')")
    @Operation(summary = "Advanced search with filters")
    @GetMapping("/advanced-search")
    public ApiResponse<PageResponse<AccountResponse>> advancedSearch(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String searchBy,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Boolean isDeleted,
            @RequestParam(required = false) String createdDateFrom,
            @RequestParam(required = false) String createdDateTo,
            @RequestParam(required = false) String lastLoginFrom,
            @RequestParam(required = false) String lastLoginTo,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<AccountResponse>>builder()
                .code(1000)
                .message("Users fetched successfully")
                .result(accountService.advancedSearch(search, searchBy, role, status, gender, isDeleted,
                        createdDateFrom, createdDateTo, lastLoginFrom, lastLoginTo, sortBy, sortOrder, page, size))
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CONSULTANT')")
    @Operation(summary = "Check password strength")
    @PostMapping("/check-password-strength")
    public ApiResponse<PasswordStrengthResponse> checkPasswordStrength(@RequestBody @Valid PasswordStrengthRequest request) {
        return ApiResponse.<PasswordStrengthResponse>builder()
                .code(1011)
                .message("Password strength checked successfully")
                .result(accountService.checkPasswordStrength(request.getPassword()))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Toggle user status (ACTIVE/INACTIVE)")
    @PatchMapping("/{accountId}/toggle-status")
    public ApiResponse<AccountResponse> toggleUserStatus(@PathVariable UUID accountId) {
        return ApiResponse.<AccountResponse>builder()
                .code(1003)
                .message("User status toggled successfully")
                .result(accountService.toggleUserStatus(accountId))
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CONSULTANT')")
    @Operation(summary = "Get account statistics")
    @GetMapping("/stats")
    public ApiResponse<AccountStatsResponse> getAccountStats() {
        return ApiResponse.<AccountStatsResponse>builder()
                .code(1000)
                .message("Account statistics fetched successfully")
                .result(accountService.getAccountStats())
                .build();
    }
}

