package com.example.SBA_M.security;

import com.example.SBA_M.entity.commands.Account;
import com.example.SBA_M.repository.commands.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set; // Import Set
import java.util.stream.Collectors; // Import Collectors

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // Giả định AccountRepository có phương thức findByUsernameOrEmail
        // để tìm tài khoản bằng username HOẶC email
        Account account = accountRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail));

        // Kiểm tra trạng thái tài khoản.
        // Sử dụng account.getStatus() trực tiếp so sánh với enum AccountStatus.ACTIVE
        if (account.getStatus() != com.example.SBA_M.utils.AccountStatus.ACTIVE) {
            throw new UsernameNotFoundException("Account is not active. Status: " + account.getStatus().name());
        }

        // Tạo GrantedAuthority từ Set<Role> của Account
        // Spring Security yêu cầu vai trò bắt đầu bằng "ROLE_"
        Set<GrantedAuthority> authorities = account.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().name())) // role.getName() là enum, cần gọi .name() để lấy String
                .collect(Collectors.toSet()); // Thu thập thành một Set để đảm bảo các quyền là duy nhất

        return new User(
                account.getUsername(), // Sử dụng username làm principal (tên người dùng chính)
                account.getPassword(), // Sử dụng getPassword() thay vì getPasswordHash()
                authorities // Truyền vào tập hợp các GrantedAuthority
        );
    }
}