package com.example.SBA_M.service.profile;

import com.example.SBA_M.entity.commands.profile.UserProfileImage;

import java.util.List;

public interface UserProfileImageService {
    UserProfileImage saveUserProfileImage(Long userProfileId, String imageUrl, String imageName, UserProfileImage.ImageType imageType);

    List<UserProfileImage> getUserProfileImages(Long userProfileId);
}
