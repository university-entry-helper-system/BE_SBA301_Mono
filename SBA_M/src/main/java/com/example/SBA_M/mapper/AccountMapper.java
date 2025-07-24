package com.example.SBA_M.mapper;

import com.example.SBA_M.dto.request.AccountCreationRequest;
import com.example.SBA_M.dto.request.UserUpdateRequest;
import com.example.SBA_M.dto.response.AccountResponse;
import com.example.SBA_M.dto.response.RoleResponse;
import com.example.SBA_M.entity.commands.Account;
import com.example.SBA_M.entity.commands.Role;
import com.example.SBA_M.utils.Gender;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

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
    @Mapping(target = "dob", source = "dob", qualifiedByName = "stringToLocalDate")
    Account toAccount(AccountCreationRequest request);

    // Chuyển đổi Account entity sang AccountResponse DTO
    // Bỏ qua trường 'password' vì nó là thông tin nhạy cảm
    @Mapping(target = "role", source = "roles", qualifiedByName = "rolesToRoleResponse")
    @Mapping(target = "gender", source = "gender", qualifiedByName = "genderToString")
    @Mapping(target = "loginCount", source = "loginCount")
    @Mapping(target = "lastLoginAt", source = "lastLoginAt")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "updatedBy", source = "updatedBy")
    @Mapping(target = "remainingConsultations", source = "remainingConsultations")
    AccountResponse toAccountResponse(Account account);

    // Cập nhật một Account entity hiện có từ UserUpdateRequest
    // Bỏ qua các trường không nên được cập nhật trực tiếp từ yêu cầu người dùng
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true) // Username không nên thay đổi qua update profile
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
    @Mapping(target = "dob", ignore = true)
    @Mapping(target = "gender", ignore = true)
    void updateAccount(@MappingTarget Account account, UserUpdateRequest request);

    @Named("stringToLocalDate")
    default LocalDate stringToLocalDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            return LocalDate.parse(dateStr, formatter);
        } catch (Exception e) {
            return null;
        }
    }

    @Named("rolesToRoleResponse")
    default RoleResponse rolesToRoleResponse(Set<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return null;
        }
        // Lấy role đầu tiên (thường user chỉ có 1 role)
        Role firstRole = roles.iterator().next();
        RoleResponse response = new RoleResponse();
        response.setId(firstRole.getId());
        response.setName(firstRole.getName().name());
        return response;
    }

    @Named("genderToString")
    default String genderToString(Gender gender) {
        return gender != null ? gender.name() : null;
    }
}