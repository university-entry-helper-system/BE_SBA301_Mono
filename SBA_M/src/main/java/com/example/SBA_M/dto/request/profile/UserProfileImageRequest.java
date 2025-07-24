package com.example.SBA_M.dto.request.profile;

import com.example.SBA_M.entity.commands.profile.UserProfileImage.ImageType;  // Sử dụng đúng enum từ UserProfileImage
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;



@Data
public class UserProfileImageRequest {


    private ImageType imageType;  // Sử dụng ImageType từ UserProfileImage


    private MultipartFile imageFile;  // Tệp ảnh người dùng tải lên
}