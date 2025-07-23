package com.example.SBA_M.service.profile;

import com.example.SBA_M.entity.commands.profile.UserProfile;
import com.example.SBA_M.entity.commands.profile.UserProfileImage;
import com.example.SBA_M.utils.Gender;

import java.time.LocalDateTime;
import java.util.List;

public interface UserProfileService {
    UserProfile updateUserProfile(Long userId, String firstName, String lastName, String email,
                                  String phone, String identityCard, Gender gender, LocalDateTime dob);

    UserProfileImage saveUserProfileImage(Long userProfileId, String imageUrl, String imageName, UserProfileImage.ImageType imageType);

    List<UserProfileImage> getUserProfileImages(Long userProfileId);
}