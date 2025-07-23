package com.example.SBA_M.service.impl.profile;


import com.example.SBA_M.dto.request.profile.UserProfileCreateRequest;
import com.example.SBA_M.entity.commands.profile.UserProfile;
import com.example.SBA_M.mapper.UserProfileMapper;
import com.example.SBA_M.repository.commands.profile.UserProfileRepository;

import com.example.SBA_M.service.profile.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;

    @Override
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