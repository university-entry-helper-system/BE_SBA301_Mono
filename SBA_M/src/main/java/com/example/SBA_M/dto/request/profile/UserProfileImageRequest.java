package com.example.SBA_M.dto.request.profile;

import com.example.SBA_M.entity.commands.profile.UserProfileImage.ImageType;  // Sử dụng đúng enum từ UserProfileImage
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
public class UserProfileImageRequest {

    @NotNull(message = "Loại ảnh không được để trống")
    private ImageType imageType;  // Sử dụng ImageType từ UserProfileImage

    @NotNull(message = "Ảnh không được để trống")
    private MultipartFile imageFile;  // Tệp ảnh người dùng tải lên
}