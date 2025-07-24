package com.example.SBA_M.service.profile;


import com.example.SBA_M.dto.request.profile.UserProfileCreateRequest;
import com.example.SBA_M.dto.request.profile.UserProfileUpdateRequest;
import com.example.SBA_M.entity.commands.profile.UserProfile;

public interface UserProfileService {
    UserProfile createUserProfile(UserProfileCreateRequest request);
    public void deleteUserProfile(Long userProfileId);
    public UserProfile updateUserProfile(Long userProfileId, UserProfileUpdateRequest updatedUserProfile);
    public UserProfile getUserProfileByAccountId(String accountId);
    public UserProfile getUserProfileById(Long userProfileId);
}