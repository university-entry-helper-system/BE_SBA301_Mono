package com.example.SBA_M.repository.commands.profile;

import com.example.SBA_M.entity.commands.profile.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    // Kiểm tra xem mã hồ sơ đã tồn tại chưa
    boolean existsByProfileCode(String profileCode);
}
