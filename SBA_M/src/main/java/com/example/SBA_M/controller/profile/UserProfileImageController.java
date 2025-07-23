package com.example.SBA_M.controller.profile;

import com.example.SBA_M.dto.request.profile.GetUserProfileImageRequest;
import com.example.SBA_M.dto.request.profile.UserProfileImageListRequest;
import com.example.SBA_M.dto.response.ApiResponse;
import com.example.SBA_M.dto.response.profile.UserProfileImageResponse;
import com.example.SBA_M.service.profile.UserProfileImageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/user-profile")
@RequiredArgsConstructor
public class UserProfileImageController {

    private final UserProfileImageService userProfileImageService;

    @Operation(summary = "Thêm ảnh vào hồ sơ người dùng", description = "Tải ảnh lên MinIO và trả về URL của ảnh đã tải lên.")
    @PostMapping("/{userProfileId}/images")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserProfileImageResponse> addImageToUserProfile(
            @PathVariable Long userProfileId,
            @Valid @RequestBody UserProfileImageListRequest.UserProfileImageRequest request) {

        // Gọi service để xử lý thêm ảnh vào hồ sơ người dùng và lấy URL
        UserProfileImageResponse response = userProfileImageService.addImageToUserProfile(userProfileId, request);

        // Trả về URL ảnh
        return ApiResponse.<UserProfileImageResponse>builder()
                .code(1000)
                .message("Thêm ảnh vào hồ sơ thành công")
                .result(response)
                .build();
    }

    @Operation(summary = "Lấy ảnh theo loại và UserProfile ID", description = "Trả về ảnh từ MinIO theo loại ảnh và ID hồ sơ người dùng.")
    @PostMapping("/images")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<UserProfileImageResponse> getImageByType(@RequestBody GetUserProfileImageRequest request) {

        // Gọi service để lấy ảnh theo UserProfileId và imageType
        UserProfileImageResponse response = userProfileImageService.getImageByType(request);

        // Trả về URL ảnh và thông tin ảnh
        return ApiResponse.<UserProfileImageResponse>builder()
                .code(1000)
                .message("Lấy ảnh thành công")
                .result(response)
                .build();
    }
}


