package com.example.SBA_M.repository.commands.profile;

import com.example.SBA_M.entity.commands.profile.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    UserProfile findByUserId(Long userId);  // Tìm UserProfile theo userId

    boolean existsByProfileCode(String profileCode);  // Kiểm tra trùng mã hồ sơ
}
