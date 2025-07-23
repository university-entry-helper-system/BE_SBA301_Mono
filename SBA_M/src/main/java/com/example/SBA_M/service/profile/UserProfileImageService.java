package com.example.SBA_M.service.profile;

import com.example.SBA_M.dto.request.profile.GetUserProfileImageRequest;
import com.example.SBA_M.dto.request.profile.UserProfileImageListRequest;
import com.example.SBA_M.dto.response.profile.UserProfileImageResponse;
import com.example.SBA_M.entity.commands.profile.UserProfileImage;

import java.util.List;

public interface UserProfileImageService {
    public UserProfileImageResponse addImageToUserProfile(Long userProfileId, UserProfileImageListRequest.UserProfileImageRequest request);
    UserProfileImageResponse getImageByType(GetUserProfileImageRequest request);
}
