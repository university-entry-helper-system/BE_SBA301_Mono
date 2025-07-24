package com.example.SBA_M.dto.request.profile;


import com.example.SBA_M.entity.commands.profile.UserProfile;
import lombok.Data;


import java.time.LocalDate;

@Data
public class UserProfileUpdateRequest {

    private String firstName;  // Tên đầu (nullable)

    private String lastName;   // Họ tên (nullable)


    private LocalDate dob;     // Ngày sinh (nullable)


    private String identityCard;  // Số chứng minh nhân dân/CCCD (nullable)


    private String email;      // Địa chỉ email (nullable)


    private String phoneNumber;  // Số điện thoại (nullable)

    private UserProfile.Gender gender;    // Giới tính (nullable)
}