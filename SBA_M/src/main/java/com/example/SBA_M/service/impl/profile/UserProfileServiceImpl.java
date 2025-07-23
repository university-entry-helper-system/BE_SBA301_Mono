package com.example.SBA_M.service.impl.profile;

import com.example.SBA_M.entity.commands.profile.UserProfile;
import com.example.SBA_M.entity.commands.profile.UserProfileImage;
import com.example.SBA_M.repository.commands.profile.UserProfileImageRepository;
import com.example.SBA_M.repository.commands.profile.UserProfileRepository;
import com.example.SBA_M.service.profile.UserProfileService;
import com.example.SBA_M.utils.Gender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserProfileImageRepository userProfileImageRepository;



    @Override
    public UserProfile updateUserProfile(Long userId, String firstName, String lastName, String email,
                                         String phone, String identityCard, Gender gender, LocalDateTime dob) {
        // Tìm UserProfile từ userId
        UserProfile userProfile = userProfileRepository.findByUserId(userId);

        // Nếu UserProfile không tồn tại, trả về null
        if (userProfile == null) {
            throw new RuntimeException("UserProfile not found for userId: " + userId);
        }

        // Cập nhật thông tin
        userProfile.setFirstName(firstName);
        userProfile.setLastName(lastName);
        userProfile.setEmail(email);
        userProfile.setPhone(phone);
        userProfile.setIdentityCard(identityCard);
        userProfile.setGender(gender);
        userProfile.setDob(dob);

        // Nếu chưa có profileCode, sinh mã hồ sơ
        if (userProfile.getProfileCode() == null || userProfile.getProfileCode().isEmpty()) {
            userProfile.setProfileCode(generateProfileCode());
        }

        // Lưu lại UserProfile sau khi cập nhật
        return userProfileRepository.save(userProfile);
    }

    @Override
    public UserProfileImage saveUserProfileImage(Long userProfileId, String imageUrl, String imageName, UserProfileImage.ImageType imageType) {
        // Tìm UserProfile theo userProfileId
        UserProfile userProfile = userProfileRepository.findById(userProfileId)
                .orElseThrow(() -> new RuntimeException("UserProfile not found for userId: " + userProfileId));

        // Tạo mới UserProfileImage và lưu lại
        UserProfileImage userProfileImage = new UserProfileImage();
        userProfileImage.setUserProfile(userProfile);
        userProfileImage.setImageUrl(imageUrl);
        userProfileImage.setImageName(imageName);
        userProfileImage.setImageType(imageType);

        // Lưu và trả về UserProfileImage
        return userProfileImageRepository.save(userProfileImage);
    }

    @Override
    public List<UserProfileImage> getUserProfileImages(Long userProfileId) {
        // Tìm UserProfile theo userProfileId
        UserProfile userProfile = userProfileRepository.findById(userProfileId)
                .orElseThrow(() -> new RuntimeException("UserProfile not found for userId: " + userProfileId));

        // Trả về tất cả hình ảnh liên quan đến UserProfile
        return userProfileImageRepository.findByUserProfile(userProfile);
    }

    // Sinh mã hồ sơ (3 chữ và 3 số)
    private String generateProfileCode() {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder code;
        String generatedCode;

        // Kiểm tra trùng lặp mã hồ sơ trong cơ sở dữ liệu
        do {
            code = new StringBuilder();

            // Sinh 3 chữ ngẫu nhiên
            for (int i = 0; i < 3; i++) {
                int index = new Random().nextInt(letters.length());
                code.append(letters.charAt(index));
            }

            // Sinh 3 số ngẫu nhiên
            for (int i = 0; i < 3; i++) {
                int number = new Random().nextInt(10);  // Random số từ 0 đến 9
                code.append(number);
            }

            generatedCode = code.toString();
        } while (userProfileRepository.existsByProfileCode(generatedCode));  // Kiểm tra xem mã đã tồn tại hay chưa

        return generatedCode;
    }
}