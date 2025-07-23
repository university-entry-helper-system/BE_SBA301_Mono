package com.example.SBA_M.service.profile;


import com.example.SBA_M.dto.request.profile.UserProfileCreateRequest;
import com.example.SBA_M.entity.commands.profile.UserProfile;

public interface UserProfileService {
    UserProfile createUserProfile(UserProfileCreateRequest request); // Tạo hồ sơ người dùng
}