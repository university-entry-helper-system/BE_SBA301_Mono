package com.example.SBA_M.dto.request.profile;


import com.example.SBA_M.entity.commands.profile.UserProfile;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class UserProfileUpdateRequest {

    private String firstName;  // Tên đầu (nullable)

    private String lastName;   // Họ tên (nullable)

    @Past(message = "Date of birth must be a past date")
    private LocalDate dob;     // Ngày sinh (nullable)

    @Size(min = 9, max = 12, message = "Identity card should be between 9 and 12 digits")
    private String identityCard;  // Số chứng minh nhân dân/CCCD (nullable)

    @Email(message = "Invalid email format")
    private String email;      // Địa chỉ email (nullable)

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number should be between 10 to 15 digits")
    private String phoneNumber;  // Số điện thoại (nullable)

    private UserProfile.Gender gender;    // Giới tính (nullable)
}