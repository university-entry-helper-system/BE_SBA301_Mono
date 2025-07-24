package com.example.SBA_M.service.impl.profile;


import com.example.SBA_M.dto.request.profile.GetUserProfileImageRequest;
import com.example.SBA_M.dto.request.profile.UserProfileCreateRequest;
import com.example.SBA_M.dto.request.profile.UserProfileUpdateRequest;
import com.example.SBA_M.entity.commands.profile.UserProfile;
import com.example.SBA_M.entity.commands.profile.UserProfileImage;
import com.example.SBA_M.mapper.UserProfileMapper;
import com.example.SBA_M.repository.commands.profile.UserProfileImageRepository;
import com.example.SBA_M.repository.commands.profile.UserProfileRepository;

import com.example.SBA_M.service.minio.MinioService;
import com.example.SBA_M.service.profile.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserProfileImageRepository userProfileImageRepository;
    private final MinioService minioService;


    @Override
    public UserProfile getUserProfileByAccountId(String accountId){
        // Tìm kiếm UserProfile theo accountId
        return userProfileRepository.findByUserId(UUID.fromString(accountId));
    }

    @Override
    public UserProfile getUserProfileById(Long userProfileId) {
        // Tìm kiếm UserProfile theo ID
        return userProfileRepository.findById(userProfileId)
                .orElseThrow(() -> new IllegalArgumentException("UserProfile không tồn tại với ID: " + userProfileId));
    }











    @Override
    @Transactional
    public UserProfile createUserProfile(UserProfileCreateRequest request) {
        // Tạo mã hồ sơ tự động
        String profileCode = generateProfileCode();

        // Kiểm tra nếu mã hồ sơ đã tồn tại
        while (userProfileRepository.existsByProfileCode(profileCode)) {
            profileCode = generateProfileCode(); // Tạo lại mã nếu bị trùng
        }

        // Sử dụng mapper để chuyển từ DTO sang Entity
        UserProfile userProfile = UserProfileMapper.mapToEntity(request, profileCode);

        // Lưu vào cơ sở dữ liệu
        UserProfile savedUserProfile = userProfileRepository.save(userProfile);

        // Trả về thông tin hồ sơ người dùng đã tạo
        return savedUserProfile;
    }


    @Override
    @Transactional
    public UserProfile updateUserProfile(Long userProfileId, UserProfileUpdateRequest updatedUserProfile) {
        // Kiểm tra xem UserProfile có tồn tại không
        UserProfile existingUserProfile = userProfileRepository.findById(userProfileId)
                .orElseThrow(() -> new IllegalArgumentException("UserProfile không tồn tại với ID: " + userProfileId));

        // Cập nhật các trường từ updatedUserProfile vào existingUserProfile
        existingUserProfile.setFirstName(updatedUserProfile.getFirstName());
        existingUserProfile.setLastName(updatedUserProfile.getLastName());
        existingUserProfile.setDob(updatedUserProfile.getDob());
        existingUserProfile.setIdentityCard(updatedUserProfile.getIdentityCard());
        existingUserProfile.setEmail(updatedUserProfile.getEmail());
        existingUserProfile.setPhone(updatedUserProfile.getPhoneNumber());
        existingUserProfile.setGender(updatedUserProfile.getGender()); // Cập nhật giới tính

        // Lưu lại UserProfile đã được cập nhật
        return userProfileRepository.save(existingUserProfile);
    }




    @Override
    @Transactional
    public void deleteUserProfile(Long userProfileId) {
        // Kiểm tra xem UserProfile có tồn tại không
        UserProfile userProfile = userProfileRepository.findById(userProfileId)
                .orElseThrow(() -> new IllegalArgumentException("UserProfile không tồn tại với ID: " + userProfileId));

        // Xóa ảnh liên quan đến UserProfile
        userProfileImageRepository.deleteByUserProfileId(userProfileId);

        // Xóa UserProfile
        userProfileRepository.delete(userProfile);
    }



    // Hàm private sinh mã hồ sơ ngẫu nhiên gồm 6 ký tự chữ và số
    private String generateProfileCode() {
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String digits = "0123456789";

        // Sinh 3 chữ cái ngẫu nhiên
        for (int i = 0; i < 3; i++) {
            code.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }

        // Sinh 3 chữ số ngẫu nhiên
        for (int i = 0; i < 3; i++) {
            code.append(digits.charAt(random.nextInt(digits.length())));
        }

        return code.toString    ();
    }
}