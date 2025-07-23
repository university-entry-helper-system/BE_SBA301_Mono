package com.example.SBA_M.dto.request.profile;

import com.example.SBA_M.entity.commands.profile.UserProfile;
import com.example.SBA_M.utils.Gender;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class UserProfileCreateRequest {

    @NotBlank(message = "Họ không được để trống")
    private String firstName;

    @NotBlank(message = "Tên không được để trống")
    private String lastName;

    @Email(message = "Email không hợp lệ")
    @NotBlank(message = "Email không được để trống")
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String phone;

    @NotBlank(message = "CCCD không được để trống")
    private String identityCard;

    @NotNull(message = "Ngày sinh không được để trống")
    private LocalDateTime dob;

    @NotNull(message = "Giới tính không được để trống")
    private UserProfile.Gender gender;
}