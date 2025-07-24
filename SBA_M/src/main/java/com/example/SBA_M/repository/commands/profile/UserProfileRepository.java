package com.example.SBA_M.repository.commands.profile;

import com.example.SBA_M.dto.request.profile.UserProfileUpdateRequest;
import com.example.SBA_M.entity.commands.profile.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    // Kiểm tra xem mã hồ sơ đã tồn tại chưa
    boolean existsByProfileCode(String profileCode);
    UserProfile findByUserId(UUID userId);

}
