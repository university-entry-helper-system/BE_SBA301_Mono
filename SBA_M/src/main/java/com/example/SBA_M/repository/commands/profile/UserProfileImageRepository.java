package com.example.SBA_M.repository.commands.profile;

import com.example.SBA_M.entity.commands.profile.UserProfile;
import com.example.SBA_M.entity.commands.profile.UserProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileImageRepository extends JpaRepository<UserProfileImage, Long> {
    // Tìm tất cả hình ảnh của một UserProfile

    Optional<UserProfileImage> findByUserProfileIdAndImageType(Long userProfile_id, UserProfileImage.ImageType imageType);

    List<UserProfileImage> findByUserProfileId(Long userProfileId);

    void deleteByUserProfileId(Long userProfileId);
}

