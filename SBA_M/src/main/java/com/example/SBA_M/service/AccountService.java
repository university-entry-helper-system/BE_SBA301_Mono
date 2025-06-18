package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.AccountCreationRequest;
import com.example.SBA_M.dto.request.AccountStatusUpdateRequest; // Thêm import này
import com.example.SBA_M.dto.request.UpdatePasswordRequest;
import com.example.SBA_M.dto.request.UserUpdateRequest;
import com.example.SBA_M.dto.response.AccountResponse;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.utils.AccountStatus; // Thêm import này

import java.util.Set; // Thay đổi từ List sang Set cho roles
import java.util.UUID;

public interface AccountService {
    AccountResponse createGeneralUser(AccountCreationRequest request);
    AccountResponse createAdminUser(AccountCreationRequest request);
    PageResponse<AccountResponse> getAllUsers(int page, int size);
    PageResponse<AccountResponse> getUsersBySearch(String name, int page, int size);
    AccountResponse getUserById(UUID accountId);
    void deleteUser(UUID accountId, boolean hardDelete); // Bổ sung tham số hardDelete
    void restoreUser(UUID accountId); // Thêm phương thức restoreUser
    AccountResponse updateUser(UUID accountId, UserUpdateRequest request);
    AccountResponse setRoles(UUID accountId, Set<Integer> roleIds); // Đổi tên và kiểu List sang Set
    Set<String> getUserRoles(UUID accountId); // Thêm phương thức getUserRoles
    AccountResponse updateStatus(UUID accountId, AccountStatus status); // Thêm phương thức updateStatus
    AccountResponse getMyInfo(String username);
    void updatePassword(String username, UpdatePasswordRequest updatePasswordRequest);
    AccountResponse updateProfile(String username, UserUpdateRequest request); // Thêm phương thức updateProfile
}