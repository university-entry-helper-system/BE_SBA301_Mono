package com.example.SBA_M.dto.response.profile;

import com.example.SBA_M.entity.commands.profile.UserProfile;
import com.example.SBA_M.utils.Gender;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserProfileResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String identityCard;
    private LocalDate dob;
    private UserProfile.Gender gender;
    private String profileCode; // Mã hồ sơ tự động sinh
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}