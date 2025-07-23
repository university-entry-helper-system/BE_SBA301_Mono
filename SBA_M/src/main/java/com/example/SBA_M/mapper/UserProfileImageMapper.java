package com.example.SBA_M.mapper;

import com.example.SBA_M.dto.request.profile.UserProfileImageListRequest;
import com.example.SBA_M.dto.response.profile.UserProfileImageResponse;
import com.example.SBA_M.entity.commands.profile.UserProfile;
import com.example.SBA_M.entity.commands.profile.UserProfileImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

public class UserProfileImageMapper {

    // Chuyển từ UserProfileImageListRequest (Request DTO) sang danh sách UserProfileImage (Entity)
    public static List<UserProfileImage> mapToEntityList(UserProfile userProfile, UserProfileImageListRequest request) {
        return request.getImages().stream().map(imageRequest -> {
            return mapToEntity(userProfile, imageRequest);
        }).collect(Collectors.toList());
    }

    // Chuyển từ UserProfileImageRequest (Request DTO) sang UserProfileImage (Entity)
    private static UserProfileImage mapToEntity(UserProfile userProfile, UserProfileImageListRequest.UserProfileImageRequest request) {
        UserProfileImage userProfileImage = new UserProfileImage();

         // Gán đối tượng UserProfile thay vì ID
        userProfileImage.setImageType(request.getImageType());  // Gán loại ảnh từ Request
        userProfileImage.setImageName(request.getImageFile().getOriginalFilename());  // Tên ảnh
        // Bạn có thể gọi MinioService để tải ảnh lên MinIO và lấy URL sau này
        // userProfileImage.setImageUrl(minioService.uploadFileAndGetPresignedUrl(request.getImageFile()));

        return userProfileImage;
    }


    public static UserProfileImageResponse mapToResponse(UserProfileImage userProfileImage) {
        UserProfileImageResponse response = new UserProfileImageResponse();
        response.setImageType(userProfileImage.getImageType());  // Loại ảnh
        response.setImageUrl(userProfileImage.getImageUrl());    // URL ảnh
        response.setImageName(userProfileImage.getImageName());  // Tên ảnh
        return response;
    }
    // Chuyển từ danh sách UserProfileImage (Entity) sang danh sách UserProfileImageResponse (DTO)
    public static List<UserProfileImageResponse> mapToResponseList(List<UserProfileImage> userProfileImages) {
        return userProfileImages.stream()
                .map(UserProfileImageMapper::mapToResponse)
                .collect(Collectors.toList());
    }
}
