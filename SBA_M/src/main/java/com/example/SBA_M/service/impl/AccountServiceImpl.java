package com.example.SBA_M.service.impl;

import com.example.SBA_M.dto.request.AccountCreationRequest;
import com.example.SBA_M.dto.request.UpdatePasswordRequest;
import com.example.SBA_M.dto.request.UserUpdateRequest;
import com.example.SBA_M.dto.response.AccountResponse;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.entity.commands.Account;
import com.example.SBA_M.entity.commands.Role;
import com.example.SBA_M.exception.AppException;
import com.example.SBA_M.exception.ErrorCode;
import com.example.SBA_M.mapper.AccountMapper;
import com.example.SBA_M.repository.commands.AccountRepository;
import com.example.SBA_M.repository.commands.RoleRepository;
import com.example.SBA_M.service.AccountService;
import com.example.SBA_M.utils.AccountStatus;
import com.example.SBA_M.utils.Gender;
import com.example.SBA_M.utils.RoleName;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import com.example.SBA_M.dto.response.AccountStatsResponse;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AccountServiceImpl implements AccountService {

    AccountRepository accountRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    AccountMapper accountMapper;

    public AccountResponse createGeneralUser(AccountCreationRequest request) {
        if (accountRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }
        if (accountRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        Account account = accountMapper.toAccount(request);
        account.setPassword(passwordEncoder.encode(request.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        account.setRoles(Set.of(userRole));
        account.setStatus(AccountStatus.ACTIVE);
        account.setIsDeleted(false);
        account.setCreatedAt(Instant.now());
        account.setCreatedBy(getCurrentUsername());

        return accountMapper.toAccountResponse(accountRepository.save(account));
    }

    public AccountResponse createAdminUser(AccountCreationRequest request) {
        if (accountRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }
        if (accountRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        Account account = accountMapper.toAccount(request);
        account.setPassword(passwordEncoder.encode(request.getPassword()));

        Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        account.setRoles(Set.of(adminRole));
        account.setStatus(AccountStatus.ACTIVE);
        account.setIsDeleted(false);
        account.setCreatedAt(Instant.now());
        account.setCreatedBy(getCurrentUsername());

        return accountMapper.toAccountResponse(accountRepository.save(account));
    }

    public PageResponse<AccountResponse> getAllUsers(int page, int size, String sort) {
        Pageable pageable;
        if (sort != null && !sort.isEmpty()) {
            String[] sortParams = sort.split(",");
            String sortField = sortParams[0];
            Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        }
        Page<Account> accountPage = accountRepository.findAll(pageable);
        return PageResponse.<AccountResponse>builder()
                .page(accountPage.getNumber())
                .size(accountPage.getSize())
                .totalElements(accountPage.getTotalElements())
                .totalPages(accountPage.getTotalPages())
                .items(accountPage.getContent().stream()
                        .map(accountMapper::toAccountResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    public PageResponse<AccountResponse> getUsersBySearch(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Account> accountPage = accountRepository.findByFullNameContainingIgnoreCase(name, pageable);
        return PageResponse.<AccountResponse>builder()
                .page(accountPage.getNumber())
                .size(accountPage.getSize())
                .totalElements(accountPage.getTotalElements())
                .totalPages(accountPage.getTotalPages())
                .items(accountPage.getContent().stream()
                        .map(accountMapper::toAccountResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    public AccountResponse getUserById(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
        return accountMapper.toAccountResponse(account);
    }

    public void deleteUser(UUID accountId, boolean hardDelete) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

        if (hardDelete) {
            accountRepository.delete(account);
        } else {
            account.setIsDeleted(true);
            account.setUpdatedAt(Instant.now());
            account.setUpdatedBy(getCurrentUsername());
            accountRepository.save(account);
        }
    }

    public void restoreUser(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

        if (!account.getIsDeleted()) {
            throw new AppException(ErrorCode.ACCOUNT_NOT_DELETED);
        }

        account.setIsDeleted(false);
        account.setUpdatedAt(Instant.now());
        account.setUpdatedBy(getCurrentUsername());
        accountRepository.save(account);
    }

    public AccountResponse updateUser(UUID accountId, UserUpdateRequest request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

        accountMapper.updateAccount(account, request);
        account.setUpdatedAt(Instant.now());
        account.setUpdatedBy(getCurrentUsername());
        return accountMapper.toAccountResponse(accountRepository.save(account));
    }

    public AccountResponse setRoles(UUID accountId, Set<Integer> roleIds) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

        Set<Role> newRoles = new HashSet<>();
        if (roleIds != null && !roleIds.isEmpty()) {
            List<Role> foundRoles = roleRepository.findAllById(roleIds);
            if (foundRoles.size() != roleIds.size()) {
                throw new AppException(ErrorCode.ROLE_NOT_FOUND);
            }
            newRoles.addAll(foundRoles);
        }
        account.setRoles(newRoles);
        account.setUpdatedAt(Instant.now());
        account.setUpdatedBy(getCurrentUsername());
        return accountMapper.toAccountResponse(accountRepository.save(account));
    }

    public Set<String> getUserRoles(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
        return account.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
    }

    public AccountResponse updateStatus(UUID accountId, AccountStatus status) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

        account.setStatus(status);
        account.setUpdatedAt(Instant.now());
        account.setUpdatedBy(getCurrentUsername());
        return accountMapper.toAccountResponse(accountRepository.save(account));
    }

    public AccountResponse getMyInfo(String username) {
        String currentUsername = getCurrentUsername();
        if (!currentUsername.equals(username)) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
        return accountMapper.toAccountResponse(account);
    }

    public void updatePassword(String username, UpdatePasswordRequest request) {
        String currentUsername = getCurrentUsername();
        if (!currentUsername.equals(username)) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

        if (!passwordEncoder.matches(request.getOldPassword(), account.getPassword())) {
            throw new AppException(ErrorCode.OLD_PASSWORD_MISMATCH);
        }
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new AppException(ErrorCode.PASSWORD_MISMATCH);
        }

        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        account.setUpdatedAt(Instant.now());
        account.setUpdatedBy(getCurrentUsername());
        accountRepository.save(account);
    }

    public AccountResponse updateProfile(String username, UserUpdateRequest request) {
        String currentUsername = getCurrentUsername();
        if (!currentUsername.equals(username)) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

        accountMapper.updateAccount(account, request);
        account.setUpdatedAt(Instant.now());
        account.setUpdatedBy(getCurrentUsername());
        return accountMapper.toAccountResponse(accountRepository.save(account));
    }

    @Override
    public AccountResponse banUser(UUID accountId, boolean banned) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
        if (banned) {
            account.setStatus(AccountStatus.BANNED);
        } else {
            account.setStatus(AccountStatus.ACTIVE); // Hoặc lưu trạng thái trước khi ban nếu cần
        }
        account.setUpdatedAt(Instant.now());
        account.setUpdatedBy(getCurrentUsername());
        accountRepository.save(account);
        return accountMapper.toAccountResponse(account);
    }

    @Override
    public void resetUserPassword(UUID accountId, String newPassword) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
        account.setPassword(passwordEncoder.encode(newPassword));
        account.setUpdatedAt(Instant.now());
        account.setUpdatedBy(getCurrentUsername());
        accountRepository.save(account);
        // TODO: Gửi email xác thực/reset password cho user (cần tích hợp MailService)
    }

    @Override
    public com.example.SBA_M.dto.response.BulkActionResponse executeBulkAction(com.example.SBA_M.dto.request.BulkActionRequest request) {
        int affected = 0;
        if (request.getAction() == null || request.getIds() == null) return new com.example.SBA_M.dto.response.BulkActionResponse();
        for (String idStr : request.getIds()) {
            try {
                UUID id = UUID.fromString(idStr);
                switch (request.getAction()) {
                    case "ACTIVATE":
                        banUser(id, false); affected++; break;
                    case "DEACTIVATE":
                        Account acc = accountRepository.findById(id).orElse(null);
                        if (acc != null) {
                            acc.setStatus(AccountStatus.INACTIVE);
                            accountRepository.save(acc); affected++;
                        }
                        break;
                    case "BAN":
                        banUser(id, true); affected++; break;
                    case "EXPORT":
                        // TODO: Export logic (Excel)
                        break;
                    case "SEND_NOTIFICATION":
                        // TODO: Notification logic
                        break;
                }
            } catch (Exception ignored) {}
        }
        com.example.SBA_M.dto.response.BulkActionResponse resp = new com.example.SBA_M.dto.response.BulkActionResponse();
        resp.setAffected(affected);
        return resp;
    }

    @Override
    public PageResponse<com.example.SBA_M.dto.response.ActivityLogResponse> getActivityLog(UUID accountId, int page, int size) {
        // Dummy data, thực tế lấy từ bảng log
        List<com.example.SBA_M.dto.response.ActivityLogResponse> logs = new java.util.ArrayList<>();
        // ... add logs if needed
        return PageResponse.<com.example.SBA_M.dto.response.ActivityLogResponse>builder()
                .page(page).size(size).totalElements(0).totalPages(1).items(logs).build();
    }

    @Override
    public PageResponse<com.example.SBA_M.dto.response.LoginHistoryResponse> getLoginHistory(UUID accountId, int page, int size) {
        // Dummy data, thực tế lấy từ bảng login_history
        List<com.example.SBA_M.dto.response.LoginHistoryResponse> logs = new java.util.ArrayList<>();
        // ... add logs if needed
        return PageResponse.<com.example.SBA_M.dto.response.LoginHistoryResponse>builder()
                .page(page).size(size).totalElements(0).totalPages(1).items(logs).build();
    }

    @Override
    public PageResponse<AccountResponse> advancedSearch(String search, String searchBy, String role, String status, String gender, Boolean isDeleted,
                                                String createdDateFrom, String createdDateTo, String lastLoginFrom, String lastLoginTo, String sortBy, String sortOrder, int page, int size) {
        
        // Tạo Pageable với sorting
        Pageable pageable;
        if (sortBy != null && !sortBy.isEmpty()) {
            Sort.Direction direction = "DESC".equalsIgnoreCase(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC;
            pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        }
        
        // Parse các enum từ String
        AccountStatus statusEnum = parseAccountStatus(status);
        Gender genderEnum = parseGender(gender);
        
        // Parse date strings
        Instant createdFrom = parseInstant(createdDateFrom);
        Instant createdTo = parseInstant(createdDateTo);
        Instant loginFrom = parseInstant(lastLoginFrom);
        Instant loginTo = parseInstant(lastLoginTo);
        
        Page<Account> accountPage;
        
        // Nếu có filter role, sử dụng query riêng
        if (role != null && !role.isEmpty()) {
            try {
                RoleName roleName = RoleName.valueOf(role.toUpperCase());
                accountPage = accountRepository.findByRoleName(roleName, pageable);
                
                // Apply additional filters in Java
                List<Account> filteredAccounts = accountPage.getContent().stream()
                        .filter(acc -> search == null || search.isEmpty() || 
                                acc.getFullName().toLowerCase().contains(search.toLowerCase()) ||
                                acc.getEmail().toLowerCase().contains(search.toLowerCase()) ||
                                acc.getUsername().toLowerCase().contains(search.toLowerCase()) ||
                                (acc.getPhone() != null && acc.getPhone().toLowerCase().contains(search.toLowerCase())))
                        .filter(acc -> statusEnum == null || acc.getStatus() == statusEnum)
                        .filter(acc -> genderEnum == null || acc.getGender() == genderEnum)
                        .filter(acc -> isDeleted == null || acc.getIsDeleted().equals(isDeleted))
                        .filter(acc -> createdFrom == null || acc.getCreatedAt().isAfter(createdFrom) || acc.getCreatedAt().equals(createdFrom))
                        .filter(acc -> createdTo == null || acc.getCreatedAt().isBefore(createdTo) || acc.getCreatedAt().equals(createdTo))
                        .filter(acc -> loginFrom == null || (acc.getLastLoginAt() != null && (acc.getLastLoginAt().isAfter(loginFrom) || acc.getLastLoginAt().equals(loginFrom))))
                        .filter(acc -> loginTo == null || (acc.getLastLoginAt() != null && (acc.getLastLoginAt().isBefore(loginTo) || acc.getLastLoginAt().equals(loginTo))))
                        .toList();
                
                // Manual pagination
                int start = page * size;
                int end = Math.min(start + size, filteredAccounts.size());
                List<Account> pagedAccounts = filteredAccounts.subList(start, end);
                
                return PageResponse.<AccountResponse>builder()
                        .page(page)
                        .size(size)
                        .totalElements((long) filteredAccounts.size())
                        .totalPages((int) Math.ceil((double) filteredAccounts.size() / size))
                        .items(pagedAccounts.stream()
                                .map(accountMapper::toAccountResponse)
                                .collect(Collectors.toList()))
                        .build();
                
            } catch (IllegalArgumentException e) {
                log.warn("Invalid role: {}", role);
            }
        }
        
        // Sử dụng query chung cho các filter khác
        accountPage = accountRepository.findWithAdvancedFilters(
                search, statusEnum, genderEnum, isDeleted, pageable);
        
        // Apply date filters in Java to avoid parameter type issues
        List<Account> filteredAccounts = accountPage.getContent().stream()
                .filter(acc -> createdFrom == null || acc.getCreatedAt().isAfter(createdFrom) || acc.getCreatedAt().equals(createdFrom))
                .filter(acc -> createdTo == null || acc.getCreatedAt().isBefore(createdTo) || acc.getCreatedAt().equals(createdTo))
                .filter(acc -> loginFrom == null || (acc.getLastLoginAt() != null && (acc.getLastLoginAt().isAfter(loginFrom) || acc.getLastLoginAt().equals(loginFrom))))
                .filter(acc -> loginTo == null || (acc.getLastLoginAt() != null && (acc.getLastLoginAt().isBefore(loginTo) || acc.getLastLoginAt().equals(loginTo))))
                .collect(Collectors.toList());
        
        // Manual pagination for filtered results
        int start = page * size;
        int end = Math.min(start + size, filteredAccounts.size());
        List<Account> pagedAccounts = filteredAccounts.subList(start, end);
        
        return PageResponse.<AccountResponse>builder()
                .page(page)
                .size(size)
                .totalElements((long) filteredAccounts.size())
                .totalPages((int) Math.ceil((double) filteredAccounts.size() / size))
                .items(pagedAccounts.stream()
                        .map(accountMapper::toAccountResponse)
                        .collect(Collectors.toList()))
                .build();
    }
    
    private AccountStatus parseAccountStatus(String status) {
        if (status != null && !status.isEmpty()) {
            try {
                return AccountStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.warn("Invalid status: {}", status);
            }
        }
        return null;
    }
    
    private Gender parseGender(String gender) {
        if (gender != null && !gender.isEmpty()) {
            try {
                return Gender.valueOf(gender.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.warn("Invalid gender: {}", gender);
            }
        }
        return null;
    }
    
    private Instant parseInstant(String dateStr) {
        if (dateStr != null && !dateStr.isEmpty()) {
            try {
                return Instant.parse(dateStr);
            } catch (Exception e) {
                log.warn("Invalid date format: {}", dateStr);
            }
        }
        return null;
    }

    @Override
    public com.example.SBA_M.dto.response.PasswordStrengthResponse checkPasswordStrength(String password) {
        com.example.SBA_M.dto.response.PasswordStrengthResponse resp = new com.example.SBA_M.dto.response.PasswordStrengthResponse();
        int score = 0;
        if (password.length() >= 8) score++;
        if (password.matches(".*[A-Z].*")) score++;
        if (password.matches(".*[a-z].*")) score++;
        if (password.matches(".*\\d.*")) score++;
        if (password.matches(".*[!@#$%^&*()_+\\-={}:;'\"|,.<>/?].*")) score++;
        resp.setScore(score);
        String level;
        if (score >= 4) {
            level = "STRONG";
        } else if (score >= 3) {
            level = "MEDIUM";
        } else {
            level = "WEAK";
        }
        resp.setLevel(level);
        java.util.List<String> suggestions = new java.util.ArrayList<>();
        if (score < 5) {
            if (!password.matches(".*[A-Z].*")) suggestions.add("Add uppercase letters");
            if (!password.matches(".*[a-z].*")) suggestions.add("Add lowercase letters");
            if (!password.matches(".*\\d.*")) suggestions.add("Add numbers");
            if (!password.matches(".*[!@#$%^&*()_+\\-={}:;'\"|,.<>/?].*")) suggestions.add("Add special characters");
            if (password.length() < 12) suggestions.add("Increase password length");
        }
        resp.setSuggestions(suggestions);
        return resp;
    }

    @Override
    public AccountResponse toggleUserStatus(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
        if (account.getStatus() == AccountStatus.ACTIVE) {
            account.setStatus(AccountStatus.INACTIVE);
        } else if (account.getStatus() == AccountStatus.INACTIVE) {
            account.setStatus(AccountStatus.ACTIVE);
        }
        account.setUpdatedAt(Instant.now());
        account.setUpdatedBy(getCurrentUsername());
        accountRepository.save(account);
        return accountMapper.toAccountResponse(account);
    }

    @Override
    public AccountResponse changeUserRole(UUID accountId, String action, String reason) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

        // Lấy role hiện tại của user
        Set<Role> currentRoles = account.getRoles();
        RoleName currentRoleName = currentRoles.isEmpty() ? null : currentRoles.iterator().next().getName();

        // Validate và thực hiện thay đổi role
        if ("PROMOTE".equals(action)) {
            // Chỉ cho phép nâng USER lên CONSULTANT
            if (currentRoleName != RoleName.USER) {
                throw new AppException(ErrorCode.INVALID_OPERATION, "Only USER can be promoted to CONSULTANT");
            }
            
            // Tìm role CONSULTANT
            Role consultantRole = roleRepository.findByName(RoleName.CONSULTANT)
                    .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND, "CONSULTANT role not found"));
            
            Set<Role> newRoles = new HashSet<>();
            newRoles.add(consultantRole);
            account.setRoles(newRoles);
            
        } else if ("DEMOTE".equals(action)) {
            // Chỉ cho phép hạ CONSULTANT xuống USER
            if (currentRoleName != RoleName.CONSULTANT) {
                throw new AppException(ErrorCode.INVALID_OPERATION, "Only CONSULTANT can be demoted to USER");
            }
            
            // Tìm role USER
            Role userRole = roleRepository.findByName(RoleName.USER)
                    .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND, "USER role not found"));
            
            Set<Role> newRoles = new HashSet<>();
            newRoles.add(userRole);
            account.setRoles(newRoles);
            
        } else {
            throw new AppException(ErrorCode.INVALID_OPERATION, "Invalid action. Must be PROMOTE or DEMOTE");
        }

        // Cập nhật thông tin
        account.setUpdatedAt(Instant.now());
        account.setUpdatedBy(getCurrentUsername());
        
        // Log reason nếu có
        if (reason != null && !reason.trim().isEmpty()) {
            log.info("Role change for user {}: {} - Reason: {}", account.getUsername(), action, reason);
        }

        return accountMapper.toAccountResponse(accountRepository.save(account));
    }

    @Override
    public AccountStatsResponse getAccountStats() {
        // Đếm tổng số tài khoản
        Long totalAccounts = accountRepository.count();
        
        // Đếm theo từng status
        Long activeAccounts = accountRepository.countByStatusAndIsDeletedFalse(AccountStatus.ACTIVE);
        Long inactiveAccounts = accountRepository.countByStatusAndIsDeletedFalse(AccountStatus.INACTIVE);
        Long bannedAccounts = accountRepository.countByStatusAndIsDeletedFalse(AccountStatus.BANNED);
        
        // Đếm tài khoản đã xóa (soft delete)
        Long deletedAccounts = accountRepository.countByIsDeletedTrue();
        
        return new AccountStatsResponse(
                totalAccounts,
                activeAccounts,
                inactiveAccounts,
                bannedAccounts,
                deletedAccounts
        );
    }

    // Helper method for @PreAuthorize
    public boolean isAccountOwner(UUID accountId) {
        String currentUsername = getCurrentUsername();
        return accountRepository.findById(accountId)
                .map(account -> account.getUsername().equals(currentUsername))
                .orElse(false);
    }

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
