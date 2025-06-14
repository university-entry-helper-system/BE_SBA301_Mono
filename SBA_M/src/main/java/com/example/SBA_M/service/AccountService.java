package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.AccountCreationRequest;
import com.example.SBA_M.dto.request.UpdatePasswordRequest;
import com.example.SBA_M.dto.request.UserUpdateRequest;
import com.example.SBA_M.dto.response.AccountResponse;
import com.example.SBA_M.dto.response.PageResponse;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    AccountResponse createGeneralUser(AccountCreationRequest request);
    AccountResponse createAdminUser(AccountCreationRequest request);
    PageResponse<AccountResponse> getAllUsers(int page, int size);
    PageResponse<AccountResponse> getUsersBySearch(String name, int page, int size);
    AccountResponse getUserById(UUID accountId);
    void deleteUser(UUID accountId);
    AccountResponse updateUser(UUID accountId, UserUpdateRequest request);
    AccountResponse setRole(UUID accountId, List<Long> roleIds);
    AccountResponse getMyInfo(String username);
    void updatePassword(String username, UpdatePasswordRequest updatePasswordRequest);
}