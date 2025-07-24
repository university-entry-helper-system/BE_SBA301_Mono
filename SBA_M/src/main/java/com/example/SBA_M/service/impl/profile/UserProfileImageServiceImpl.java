package com.example.SBA_M.service.impl.profile;

import com.example.SBA_M.dto.request.profile.GetUserProfileImageRequest;
import com.example.SBA_M.dto.request.profile.UserProfileImageListRequest;
import com.example.SBA_M.dto.response.profile.UserProfileImageResponse;
import com.example.SBA_M.entity.commands.profile.UserProfile;
import com.example.SBA_M.entity.commands.profile.UserProfileImage;
import com.example.SBA_M.mapper.UserProfileImageMapper;

import com.example.SBA_M.repository.commands.profile.UserProfileImageRepository;
import com.example.SBA_M.repository.commands.profile.UserProfileRepository;
import com.example.SBA_M.service.minio.MinioService;
import com.example.SBA_M.service.profile.UserProfileImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfileImageServiceImpl implements UserProfileImageService {

    private final UserProfileRepository userProfileRepository;
    private final UserProfileImageRepository userProfileImageRepository;
    private final MinioService minioService;


    @Override
    @Transactional
    public UserProfileImageResponse addImageToUserProfile(Long userProfileId, UserProfileImageListRequest.UserProfileImageRequest request) {
        // Kiểm tra xem UserProfile có tồn tại không
        UserProfile userProfile = userProfileRepository.findById(userProfileId)
                .orElseThrow(() -> new IllegalArgumentException("UserProfile không tồn tại với ID: " + userProfileId));


        Optional<UserProfileImage> existingImage = userProfileImageRepository
                .findByUserProfileIdAndImageType(userProfileId, request.getImageType());

        // Nếu ảnh đã tồn tại, trả về thông báo lỗi hoặc không làm gì
        if (existingImage.isPresent()) {
            throw new IllegalArgumentException("Ảnh với loại " + request.getImageType() + " đã tồn tại cho UserProfile ID: " + userProfileId);
        }


        // Lưu ảnh lên MinIO và lấy URL
        MultipartFile file = request.getImageFile();
        String imageUrl = minioService.uploadFileAndGetPresignedUrl(file);  // Lấy URL ảnh từ MinIO

        // Lưu thông tin ảnh vào cơ sở dữ liệu
        UserProfileImage userProfileImage = new UserProfileImage();
        userProfileImage.setUserProfile(userProfile);  // Liên kết với UserProfile
        userProfileImage.setImageType(request.getImageType());  // Loại ảnh
        userProfileImage.setImageName(file.getOriginalFilename());  // Tên ảnh
        userProfileImage.setImageUrl(imageUrl);  // Lưu URL ảnh vào cơ sở dữ liệu

        // Lưu ảnh vào cơ sở dữ liệu
        userProfileImageRepository.save(userProfileImage);

        // Sử dụng mapper để chuyển Entity sang DTO (UserProfileImageResponse)
        return UserProfileImageMapper.mapToResponse(userProfileImage);
    }

    @Override
    public UserProfileImageResponse getImageByType(GetUserProfileImageRequest request) {
        // Kiểm tra xem UserProfile có tồn tại không
        UserProfile userProfile = userProfileRepository.findById(request.getUserProfileId())
                .orElseThrow(() -> new IllegalArgumentException("UserProfile không tồn tại với ID: " + request.getUserProfileId()));

        // Tìm ảnh theo userProfileId và imageType
        Optional<UserProfileImage> userProfileImageOpt = userProfileImageRepository
                .findByUserProfileIdAndImageType(request.getUserProfileId(), request.getImageType());

        // Nếu không tìm thấy ảnh, ném lỗi
        if (!userProfileImageOpt.isPresent()) {
            throw new IllegalArgumentException("Ảnh với loại " + request.getImageType() + " không tồn tại cho UserProfile ID: " + request.getUserProfileId());
        }

        UserProfileImage userProfileImage = userProfileImageOpt.get();

        // Trả về URL của ảnh (presigned URL từ MinIO)
        String imageUrl = userProfileImage.getImageUrl();  // Presigned URL đã lưu trong cơ sở dữ liệu

        // Tạo và trả về UserProfileImageResponse
        UserProfileImageResponse response = new UserProfileImageResponse();
        response.setImageType(userProfileImage.getImageType());
        response.setImageUrl(imageUrl);
        response.setImageName(userProfileImage.getImageName());

        return response;
    }

    @Override
    @Transactional
    public void deleteImage(GetUserProfileImageRequest request) {
        // Tìm ảnh theo userProfileId và imageType
        UserProfileImage userProfileImage = userProfileImageRepository
                .findByUserProfileIdAndImageType(request.getUserProfileId(), request.getImageType())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy ảnh với loại " + request.getImageType() + " cho UserProfile ID: " + request.getUserProfileId()));

        // Xóa ảnh trên MinIO
        minioService.deleteFileFromMinIO(userProfileImage.getImageName());

        // Xóa ảnh trong cơ sở dữ liệu
        userProfileImageRepository.delete(userProfileImage);
    }


    @Override
    @Transactional
    public void deleteImagesByUserProfileId(Long userProfileId) {
        // Lấy danh sách các ảnh liên quan đến UserProfile
        List<UserProfileImage> userProfileImages = userProfileImageRepository.findByUserProfileId(userProfileId);

        // Xóa các ảnh khỏi MinIO trước
        for (UserProfileImage userProfileImage : userProfileImages) {
            minioService.deleteFileFromMinIO(userProfileImage.getImageName());  // Xóa ảnh khỏi MinIO
        }

        // Xóa ảnh trong cơ sở dữ liệu
        userProfileImageRepository.deleteByUserProfileId(userProfileId);
    }


    @Override
    @Transactional
    public UserProfileImageResponse updateImageForUserProfile(Long userProfileId, UserProfileImageListRequest.UserProfileImageRequest request) {
        // Kiểm tra xem UserProfile có tồn tại không
        UserProfile userProfile = userProfileRepository.findById(userProfileId)
                .orElseThrow(() -> new IllegalArgumentException("UserProfile không tồn tại với ID: " + userProfileId));

        // Kiểm tra xem ảnh với loại imageType đã tồn tại chưa
        UserProfileImage existingImage = userProfileImageRepository
                .findByUserProfileIdAndImageType(userProfileId, request.getImageType())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy ảnh với loại " + request.getImageType() + " cho UserProfile ID: " + userProfileId));

        // Xóa ảnh cũ trên MinIO (nếu cần)
        minioService.deleteFileFromMinIO(existingImage.getImageName());  // Xóa ảnh cũ khỏi MinIO

        // Lưu ảnh mới lên MinIO và lấy URL
        MultipartFile file = request.getImageFile();
        String imageUrl = minioService.uploadFileAndGetPresignedUrl(file);  // Lấy URL ảnh từ MinIO

        // Cập nhật thông tin ảnh trong cơ sở dữ liệu
        existingImage.setImageName(file.getOriginalFilename());  // Tên ảnh mới
        existingImage.setImageUrl(imageUrl);  // Cập nhật URL ảnh mới

        // Lưu ảnh đã được cập nhật vào cơ sở dữ liệu
        userProfileImageRepository.save(existingImage);

        // Sử dụng mapper để chuyển Entity sang DTO (UserProfileImageResponse)
        return UserProfileImageMapper.mapToResponse(existingImage);
    }




}

