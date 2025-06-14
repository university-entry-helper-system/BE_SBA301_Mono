package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.LoginRequest;
import com.example.SBA_M.entity.Account;
import com.example.SBA_M.entity.Role;
import com.example.SBA_M.entity.Token;
import com.example.SBA_M.repository.AccountRepository;
import com.example.SBA_M.repository.RoleRepository;
import com.example.SBA_M.repository.TokenRepository;
import com.example.SBA_M.utils.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository; // Add this field
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(AccountRepository accountRepository, RoleRepository roleRepository, TokenRepository tokenRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository; // Initialize it here
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String login(LoginRequest loginRequest) {
        Optional<Account> account = accountRepository.findByUsername(loginRequest.getUsername());
        if (account.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), account.get().getPasswordHash())) {
            String token = jwtUtil.generateToken(loginRequest.getUsername());
            Token tokenEntity = new Token();
            tokenEntity.setAccount(account.get());
            tokenEntity.setToken(token);
            tokenEntity.setExpiresAt(LocalDateTime.now().plusHours(10));
            tokenRepository.save(tokenEntity);
            return token;
        }
        throw new RuntimeException("Invalid username or password");
    }

    public String register(Account account) {
        Role defaultRole = (Role) roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        account.setRole(defaultRole);
        account.setPasswordHash(passwordEncoder.encode(account.getPasswordHash()));
        accountRepository.save(account);
        return "Account registered successfully!";
    }

    public void logout(String token) {
        Optional<Token> tokenEntity = tokenRepository.findByToken(token);
        if (tokenEntity.isPresent()) {
            Token existingToken = tokenEntity.get();
            existingToken.setRevoked(true);
            tokenRepository.save(existingToken);
        } else {
            throw new RuntimeException("Token not found");
        }
    }
}