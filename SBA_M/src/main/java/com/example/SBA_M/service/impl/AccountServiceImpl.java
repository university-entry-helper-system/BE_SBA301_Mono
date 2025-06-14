package com.example.SBA_M.service.impl;

import com.example.SBA_M.dto.request.AccountCreationRequest;
import com.example.SBA_M.dto.request.UpdatePasswordRequest;
import com.example.SBA_M.dto.request.UserUpdateRequest;
import com.example.SBA_M.dto.response.AccountResponse;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.entity.Account;
import com.example.SBA_M.entity.Role;
import com.example.SBA_M.exception.AppException;
import com.example.SBA_M.exception.ErrorCode;
import com.example.SBA_M.mapper.AccountMapper;
import com.example.SBA_M.repository.AccountRepository;
import com.example.SBA_M.repository.RoleRepository;
import com.example.SBA_M.service.AccountService;
import com.example.SBA_M.utils.AccountStatus;
import com.example.SBA_M.utils.RoleName;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AccountServiceImpl implements AccountService {

    AccountRepository accountRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    AccountMapper accountMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public AccountResponse createGeneralUser(AccountCreationRequest request) {
        if (accountRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }
        if (accountRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        Account account = accountMapper.toAccount(request);
        account.setPassword(passwordEncoder.encode(request.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.USER.name())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        account.setRoles(roles);
        account.setStatus(AccountStatus.ACTIVE);
        account.setIsDeleted(false);
        account.setCreatedAt(Instant.now());
        account.setCreatedBy(getCurrentUsername());

        return accountMapper.toAccountResponse(accountRepository.save(account));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public AccountResponse createAdminUser(AccountCreationRequest request) {
        if (accountRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }
        if (accountRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        Account account = accountMapper.toAccount(request);
        account.setPassword(passwordEncoder.encode(request.getPassword()));

        Role adminRole = roleRepository.findByName(RoleName.ADMIN.name())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        account.setRoles(roles);
        account.setStatus(AccountStatus.ACTIVE);
        account.setIsDeleted(false);
        account.setCreatedAt(Instant.now());
        account.setCreatedBy(getCurrentUsername());

        return accountMapper.toAccountResponse(accountRepository.save(account));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CONSULTANT')")
    public PageResponse<AccountResponse> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Account> accountPage = accountRepository.findAll(pageable);
        // Sửa lỗi: Thêm <AccountResponse> vào builder()
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

    @PreAuthorize("hasAnyRole('ADMIN', 'CONSULTANT')")
    public PageResponse<AccountResponse> getUsersBySearch(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Account> accountPage = accountRepository.findByFullNameContainingIgnoreCase(name, pageable);
        // Sửa lỗi: Thêm <AccountResponse> vào builder()
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

    @PreAuthorize("hasAnyRole('ADMIN', 'CONSULTANT') or " +
            "(@accountServiceImpl.isAccountOwner(#accountId) and hasRole('USER'))")
    public AccountResponse getUserById(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
        return accountMapper.toAccountResponse(account);
    }

    @PreAuthorize("hasRole('ADMIN')")
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

    @PreAuthorize("hasRole('ADMIN')")
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

    @PreAuthorize("hasRole('ADMIN')")
    public AccountResponse updateUser(UUID accountId, UserUpdateRequest request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

        accountMapper.updateAccount(account, request);
        account.setUpdatedAt(Instant.now());
        account.setUpdatedBy(getCurrentUsername());
        return accountMapper.toAccountResponse(accountRepository.save(account));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public AccountResponse setRoles(UUID accountId, Set<Long> roleIds) {
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

    @PreAuthorize("hasAnyRole('ADMIN', 'CONSULTANT') or " +
            "(@accountServiceImpl.isAccountOwner(#accountId) and hasRole('USER'))")
    public Set<String> getUserRoles(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
        return account.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public AccountResponse updateStatus(UUID accountId, AccountStatus status) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

        account.setStatus(status);
        account.setUpdatedAt(Instant.now());
        account.setUpdatedBy(getCurrentUsername());
        return accountMapper.toAccountResponse(accountRepository.save(account));
    }

    @PreAuthorize("isAuthenticated()")
    public AccountResponse getMyInfo(String username) {
        String currentUsername = getCurrentUsername();
        if (!currentUsername.equals(username)) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
        return accountMapper.toAccountResponse(account);
    }

    @PreAuthorize("isAuthenticated()")
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

    @PreAuthorize("isAuthenticated()")
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