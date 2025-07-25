package com.example.SBA_M.entity.commands.profile;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.apache.pdfbox.rendering.ImageType;

@Entity
@Table(name = "user_profile_images")
@Getter
@Setter
@NoArgsConstructor
public class UserProfileImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_profile_id", nullable = false)
    private UserProfile userProfile;  // Khóa ngoại liên kết với UserProfile

    @Enumerated(EnumType.STRING)
    @Column(name = "image_type", nullable = false)
    private ImageType imageType;  // Loại ảnh (CCCD_1, CCCD_2, DGNL, THPT, HOCBA)

    @Column(name = "image_url", nullable = false)
    private String imageUrl;  // Đường dẫn URL của ảnh

    @Column(name = "image_name", nullable = false)
    private String imageName;  // Tên ảnh

    public enum ImageType {
        CCCD_1,
        CCCD_2,
        DGNL,
        THPT,
        HOCBA11,
        HOCBA12,
    }
}
