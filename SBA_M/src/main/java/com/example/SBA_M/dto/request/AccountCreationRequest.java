package com.example.SBA_M.dto.request;

import com.example.SBA_M.utils.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreationRequest {
    @NotBlank(message = "Username không được để trống")
    @Size(min = 4, max = 100, message = "Username phải có từ 4 đến 100 ký tự")
    private String username;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng (ví dụ: example@gmail.com)")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$",
            message = "Mật khẩu phải chứa ít nhất 1 chữ hoa, 1 chữ thường, 1 số và 1 ký tự đặc biệt")
    private String password;

    private String fullName;

    @Pattern(regexp = "^(0|\\+84)\\d{9}$", 
            message = "Số điện thoại phải bắt đầu bằng 0 hoặc +84 và có 10 chữ số (ví dụ: 0987654321 hoặc +84987654321)")
    private String phone;

    @Pattern(regexp = "^(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])-(19\\d{2}|20\\d{2})$", 
            message = "Ngày sinh phải theo định dạng MM-DD-YYYY và năm phải từ 1900 đến năm hiện tại (ví dụ: 01-30-2000)")
    private String dob;

    private Gender gender;

    @Schema(hidden = true)
    public LocalDate getDobAsLocalDate() {
        if (dob == null || dob.trim().isEmpty()) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            LocalDate date = LocalDate.parse(dob, formatter);
            
            // Validate year range
            int currentYear = LocalDate.now().getYear();
            if (date.getYear() < 1900 || date.getYear() > currentYear) {
                throw new IllegalArgumentException("Năm sinh phải từ 1900 đến " + currentYear);
            }
            
            return date;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Ngày sinh không đúng định dạng MM-DD-YYYY (ví dụ: 01-30-2000)");
        }
    }
}