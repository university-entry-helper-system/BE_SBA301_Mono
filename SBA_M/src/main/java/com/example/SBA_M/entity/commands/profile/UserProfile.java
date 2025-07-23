package com.example.SBA_M.entity.commands.profile;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "accounts_id", nullable = false)
    private UUID userId;  // Khóa ngoại từ bảng account (hoặc bảng tương ứng)

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;  // Email của người dùng (duy nhất)

    @Column(name = "phone", nullable = false, length = 15)
    private String phone;  // Số điện thoại của người dùng

    @Column(name = "identity_card", nullable = false, length = 12)
    private String identityCard;  // CCCD (12 ký tự)

    @Column(name = "dob", nullable = false)
    private LocalDateTime dob;  // Ngày sinh của người dùng

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;  // Giới tính của người dùng

    @Column(name = "profile_code", nullable = false, unique = true, length = 6)
    private String profileCode;  // Mã hồ sơ tự động sinh (3 chữ và 3 số ngẫu nhiên)

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
    public enum Gender {
        MALE,
        FEMALE
    }
}
