package com.example.SBA_M.mapper;



import com.example.SBA_M.dto.request.profile.UserProfileCreateRequest;
import com.example.SBA_M.dto.response.profile.UserProfileResponse;
import com.example.SBA_M.entity.commands.profile.UserProfile;
import java.util.List;
import java.util.stream.Collectors;

public class UserProfileMapper {

    /// Chuyển từ UserProfileCreateRequest (DTO) sang UserProfile (Entity)
    public static UserProfile mapToEntity(UserProfileCreateRequest request, String profileCode) {
        UserProfile userProfile = new UserProfile();
        userProfile.setFirstName(request.getFirstName());
        userProfile.setLastName(request.getLastName());
        userProfile.setEmail(request.getEmail());
        userProfile.setPhone(request.getPhone());
        userProfile.setIdentityCard(request.getIdentityCard());
        userProfile.setDob(request.getDob());
        userProfile.setGender(request.getGender());  // Sử dụng giá trị gender từ request
        userProfile.setProfileCode(profileCode);  // Gán mã hồ sơ tự động
        return userProfile;
    }
    // Chuyển từ UserProfile (Entity) sang UserProfileResponse (Response DTO)
    public static UserProfileResponse mapToResponse(UserProfile userProfile) {
        UserProfileResponse response = new UserProfileResponse();
        response.setId(userProfile.getId());
        response.setFirstName(userProfile.getFirstName());
        response.setLastName(userProfile.getLastName());
        response.setEmail(userProfile.getEmail());
        response.setPhone(userProfile.getPhone());
        response.setIdentityCard(userProfile.getIdentityCard());
        response.setDob(userProfile.getDob());
        response.setGender(userProfile.getGender());
        response.setProfileCode(userProfile.getProfileCode());
        response.setCreatedAt(userProfile.getCreatedAt());
        response.setUpdatedAt(userProfile.getUpdatedAt());
        return response;
    }

    // Chuyển từ danh sách UserProfile (Entity) sang danh sách UserProfileResponse (Response DTO)
    public static List<UserProfileResponse> mapToResponseList(List<UserProfile> userProfiles) {
        return userProfiles.stream()
                .map(UserProfileMapper::mapToResponse)
                .collect(Collectors.toList());
    }



//    // Chuyển đổi từ danh sách UserProfileCreateRequest (DTO) sang danh sách UserProfile (Entity)
//    public static List<UserProfile> mapToEntityList(List<UserProfileCreateRequest> requests) {
//        return requests.stream().map(UserProfileMapper::mapToEntity).collect(Collectors.toList());
//    }
}