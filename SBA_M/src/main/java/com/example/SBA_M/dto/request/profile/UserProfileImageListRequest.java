package com.example.SBA_M.dto.request.profile;

import com.example.SBA_M.entity.commands.profile.UserProfileImage.ImageType;  // Sử dụng đúng enum từ UserProfileImage
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UserProfileImageListRequest {

    @NotNull(message = "Danh sách ảnh không được để trống")
    private List<UserProfileImageRequest> images;  // Danh sách ảnh

    @Data
    public static class UserProfileImageRequest {
        @NotNull(message = "Loại ảnh không được để trống")
        private ImageType imageType;  // Loại ảnh (CCCD_1, CCCD_2, v.v.)

        @NotNull(message = "Ảnh không được để trống")
        private MultipartFile imageFile;  // Tệp ảnh người dùng tải lên
    }
}