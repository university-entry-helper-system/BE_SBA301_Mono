package com.example.SBA_M.dto.response.profile;

import com.example.SBA_M.entity.commands.profile.UserProfileImage.ImageType;
import lombok.Data;

@Data
public class UserProfileImageResponse {

    private ImageType imageType;  // Loại ảnh (CCCD_1, CCCD_2, v.v.)
    private String imageUrl;      // URL của ảnh đã tải lên MinIO
    private String imageName;     // Tên ảnh
}
