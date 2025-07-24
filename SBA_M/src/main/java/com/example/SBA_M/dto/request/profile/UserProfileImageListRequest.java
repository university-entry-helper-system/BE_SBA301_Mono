package com.example.SBA_M.dto.request.profile;

import com.example.SBA_M.entity.commands.profile.UserProfileImage.ImageType;  // Sử dụng đúng enum từ UserProfileImage
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@Data
public class UserProfileImageListRequest {


    private List<UserProfileImageRequest> images;  // Danh sách ảnh

    @Data
    public static class UserProfileImageRequest {

        private ImageType imageType;  // Loại ảnh (CCCD_1, CCCD_2, v.v.)


        private MultipartFile imageFile;  // Tệp ảnh người dùng tải lên
    }
}