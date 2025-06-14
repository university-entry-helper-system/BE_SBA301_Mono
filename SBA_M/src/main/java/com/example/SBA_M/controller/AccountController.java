package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.AccountCreationRequest;
import com.example.SBA_M.dto.request.AccountStatusUpdateRequest;
import com.example.SBA_M.dto.request.RoleAssignmentRequest;
import com.example.SBA_M.dto.request.UpdatePasswordRequest;
import com.example.SBA_M.dto.request.UserUpdateRequest;
import com.example.SBA_M.dto.response.AccountResponse;
import com.example.SBA_M.dto.response.MessageResponse;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class AccountController {

    AccountService accountService;

    @Operation(summary = "Create general user", description = "Admin creates a general user account")
    @PostMapping("/create-user")
    public ResponseEntity<AccountResponse> createGeneralUser(@RequestBody @Valid AccountCreationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createGeneralUser(request));
    }

    @Operation(summary = "Create admin user", description = "Admin creates another admin user")
    @PostMapping("/create-admin")
    public ResponseEntity<AccountResponse> createAdminUser(@RequestBody @Valid AccountCreationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAdminUser(request));
    }

    @Operation(summary = "Get all users", description = "Paginated list of all users")
    @GetMapping
    public ResponseEntity<PageResponse<AccountResponse>> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(accountService.getAllUsers(page, size));
    }

    @Operation(summary = "Search users by name", description = "Search users by name with pagination")
    @GetMapping("/search")
    public ResponseEntity<PageResponse<AccountResponse>> searchUsers(@RequestParam String name,
                                                                     @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(accountService.getUsersBySearch(name, page, size));
    }

    @Operation(summary = "Get user by ID", description = "Retrieve user details by ID")
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getUserById(@PathVariable UUID accountId) {
        return ResponseEntity.ok(accountService.getUserById(accountId));
    }

    @Operation(summary = "Delete user", description = "Delete user (soft or hard based on param)")
    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID accountId,
                                           @RequestParam(value = "hard", defaultValue = "false") boolean hardDelete) {
        accountService.deleteUser(accountId, hardDelete);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Restore user", description = "Restore a soft-deleted user")
    @PostMapping("/{accountId}/restore")
    public ResponseEntity<MessageResponse> restoreUser(@PathVariable UUID accountId) {
        accountService.restoreUser(accountId);
        return ResponseEntity.ok(new MessageResponse("User restored successfully."));
    }

    @Operation(summary = "Update user info", description = "Update user data by ID")
    @PutMapping("/{accountId}")
    public ResponseEntity<AccountResponse> updateUser(@PathVariable UUID accountId, @RequestBody @Valid UserUpdateRequest request) {
        return ResponseEntity.ok(accountService.updateUser(accountId, request));
    }

    @Operation(summary = "Assign roles", description = "Assign roles to a user")
    @PostMapping("/{accountId}/set-roles")
    public ResponseEntity<AccountResponse> setRoles(@PathVariable UUID accountId, @RequestBody @Valid RoleAssignmentRequest request) {
        return ResponseEntity.ok(accountService.setRoles(accountId, new HashSet<>(request.getRoleIds())));

    }

    @Operation(summary = "Get user roles", description = "Get assigned role names of user")
    @GetMapping("/{accountId}/roles")
    public ResponseEntity<Set<String>> getUserRoles(@PathVariable UUID accountId) {
        return ResponseEntity.ok(accountService.getUserRoles(accountId));
    }

    @Operation(summary = "Update user status", description = "Update account status (ACTIVE, INACTIVE, etc)")
    @PatchMapping("/{accountId}/status")
    public ResponseEntity<AccountResponse> updateUserStatus(@PathVariable UUID accountId, @RequestBody @Valid AccountStatusUpdateRequest request) {
        return ResponseEntity.ok(accountService.updateStatus(accountId, request.getStatus()));
    }

    @Operation(summary = "Get current user info", description = "Authenticated user fetches own info")
    @GetMapping("/my-info")
    public ResponseEntity<AccountResponse> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(accountService.getMyInfo(userDetails.getUsername()));
    }

    @Operation(summary = "Update password", description = "Authenticated user changes password")
    @PostMapping("/my-info/update-password")
    public ResponseEntity<MessageResponse> updateMyPassword(@AuthenticationPrincipal UserDetails userDetails,
                                                            @RequestBody @Valid UpdatePasswordRequest request) {
        accountService.updatePassword(userDetails.getUsername(), request);
        return ResponseEntity.ok(new MessageResponse("Password updated successfully."));
    }

    @Operation(summary = "Update profile", description = "Authenticated user updates profile info")
    @PutMapping("/my-info/update-profile")
    public ResponseEntity<AccountResponse> updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                                         @RequestBody @Valid UserUpdateRequest request) {
        return ResponseEntity.ok(accountService.updateProfile(userDetails.getUsername(), request));
    }
}