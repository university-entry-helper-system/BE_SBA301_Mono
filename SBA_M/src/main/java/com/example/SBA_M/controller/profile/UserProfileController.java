package com.example.SBA_M.controller.profile;

import com.example.SBA_M.dto.request.profile.UserProfileCreateRequest;
import com.example.SBA_M.dto.request.profile.UserProfileUpdateRequest;
import com.example.SBA_M.dto.response.ApiResponse;
import com.example.SBA_M.dto.response.profile.UserProfileResponse;
import com.example.SBA_M.entity.commands.profile.UserProfile;
import com.example.SBA_M.mapper.UserProfileMapper;
import com.example.SBA_M.service.profile.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v1/user-profile")
@RequiredArgsConstructor

@Validated
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Lấy hồ sơ người dùng", description = "Lấy thông tin hồ sơ của người dùng.")
    @PostMapping("/get")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserProfileResponse> getUserProfile(@RequestBody String request) {
        // Gọi service để lấy thông tin hồ sơ người dùng
        UserProfile userProfile = userProfileService.getUserProfileByAccountId(request);

        // Sử dụng mapper để chuyển Entity sang DTO response
        UserProfileResponse response = UserProfileMapper.mapToResponse(userProfile);

        // Trả về ApiResponse với thông tin hồ sơ người dùng
        return ApiResponse.<UserProfileResponse>builder()
                .code(1000)
                .message("Lấy hồ sơ người dùng thành công")
                .result(response)
                .build();
    }


    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Lấy hồ sơ người dùng", description = "Lấy thông tin hồ sơ của người dùng.")
    @GetMapping("/{userProfileId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserProfileResponse> getUserProfileByID(@RequestParam Long userProfileId) {
        // Gọi service để lấy thông tin hồ sơ người dùng
        UserProfile userProfile = userProfileService.getUserProfileById(userProfileId);

        // Sử dụng mapper để chuyển Entity sang DTO response
        UserProfileResponse response = UserProfileMapper.mapToResponse(userProfile);

        // Trả về ApiResponse với thông tin hồ sơ người dùng
        return ApiResponse.<UserProfileResponse>builder()
                .code(1000)
                .message("Lấy hồ sơ người dùng thành công")
                .result(response)
                .build();
    }




    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Tạo hồ sơ người dùng", description = "Tạo hồ sơ người dùng mới với các thông tin cá nhân.")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserProfileResponse> registerUserProfile(@RequestBody  UserProfileCreateRequest request) {
        // Gọi service để tạo hồ sơ
        UserProfile userProfile = userProfileService.createUserProfile(request);

        // Sử dụng mapper để chuyển Entity sang DTO response
        UserProfileResponse response = UserProfileMapper.mapToResponse(userProfile);

        // Trả về ApiResponse với thông tin hồ sơ đã tạo
        return ApiResponse.<UserProfileResponse>builder()
                .code(1000)
                .message("Tạo hồ sơ người dùng thành công")
                .result(response)
                .build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Xóa UserProfile và ảnh liên quan", description = "Xóa UserProfile và tất cả ảnh liên quan đến nó.")
    @DeleteMapping("/{userProfileId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void>deleteUserProfile(@PathVariable Long userProfileId) {
        // Gọi service để xóa UserProfile và ảnh liên quan
        userProfileService.deleteUserProfile(userProfileId);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Tạo hồ sơ người dùng thành công")
                .build();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Cập nhật thông tin UserProfile", description = "Cập nhật thông tin của UserProfile.")
    @PutMapping("/{userProfileId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<UserProfileResponse> updateUserProfile(
            @PathVariable Long userProfileId,
            @RequestBody UserProfileUpdateRequest updatedUserProfile) {
        // Gọi service để cập nhật UserProfile
        UserProfile userProfile = userProfileService.updateUserProfile(userProfileId, updatedUserProfile);

        // Sử dụng mapper để chuyển Entity sang DTO response
        UserProfileResponse response = UserProfileMapper.mapToResponse(userProfile);

        // Trả về ApiResponse với thông tin hồ sơ đã cập nhật
        return ApiResponse.<UserProfileResponse>builder()
                .code(1000)
                .message("Cập nhật hồ sơ người dùng thành công")
                .result(response)
                .build();
    }



}
