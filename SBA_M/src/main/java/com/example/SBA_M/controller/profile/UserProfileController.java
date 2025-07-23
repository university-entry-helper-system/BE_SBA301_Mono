package com.example.SBA_M.controller.profile;

import com.example.SBA_M.dto.request.profile.UserProfileCreateRequest;
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

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/user-profile")
@RequiredArgsConstructor
//@PreAuthorize("hasRole('ADMIN')")
@Validated
public class UserProfileController {

    private final UserProfileService userProfileService;

    @Operation(summary = "Tạo hồ sơ người dùng", description = "Tạo hồ sơ người dùng mới với các thông tin cá nhân.")
    @PostMapping("/register1")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserProfileResponse> registerUserProfile(@RequestBody @Valid UserProfileCreateRequest request) {
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




}
