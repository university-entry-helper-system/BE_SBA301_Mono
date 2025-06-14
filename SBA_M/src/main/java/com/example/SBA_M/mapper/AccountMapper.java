package com.example.SBA_M.mapper;

import com.example.SBA_M.dto.request.AccountCreationRequest;
import com.example.SBA_M.dto.request.UserUpdateRequest;
import com.example.SBA_M.dto.response.AccountResponse;
import com.example.SBA_M.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring") // Đảm bảo Spring có thể inject mapper này
public interface AccountMapper {
    // Instance của mapper, có thể bỏ qua nếu dùng @Autowired trong Spring
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    // Chuyển đổi AccountCreationRequest sang Account entity
    // Bỏ qua trường 'password' vì nó sẽ được mã hóa riêng trong service
    @Mapping(target = "password", ignore = true)
    // Các trường mặc định hoặc được xử lý ở Service, không map trực tiếp từ request
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "lastLoginAt", ignore = true)
    @Mapping(target = "loginCount", ignore = true)
    Account toAccount(AccountCreationRequest request);

    // Chuyển đổi Account entity sang AccountResponse DTO
    // Bỏ qua trường 'password' vì nó là thông tin nhạy cảm
    @Mapping(target = "password", ignore = true)
    AccountResponse toAccountResponse(Account account);

    // Cập nhật một Account entity hiện có từ UserUpdateRequest
    // Bỏ qua các trường không nên được cập nhật trực tiếp từ yêu cầu người dùng
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true) // Username không nên thay đổi qua update profile
    @Mapping(target = "email", ignore = true) // Email có thể cần logic xác minh riêng để thay đổi
    @Mapping(target = "password", ignore = true) // Mật khẩu có API riêng để cập nhật
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true) // Sẽ được service set
    @Mapping(target = "updatedBy", ignore = true) // Sẽ được service set
    @Mapping(target = "lastLoginAt", ignore = true)
    @Mapping(target = "loginCount", ignore = true)
    void updateAccount(@MappingTarget Account account, UserUpdateRequest request);
}