package com.example.SBA_M.dto.request.profile;


import com.example.SBA_M.entity.commands.profile.UserProfileImage;
import lombok.Data;

@Data
public class GetUserProfileImageRequest {

    private Long userProfileId;  // ID của userProfile
    private UserProfileImage.ImageType imageType;    // Loại ảnh (CCCD_1, CCCD_2, v.v.)
}
