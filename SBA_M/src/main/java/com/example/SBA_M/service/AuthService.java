package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.AccountCreationRequest;
import com.example.SBA_M.dto.request.LoginRequest;
import com.example.SBA_M.dto.request.RefreshTokenRequest;
import com.example.SBA_M.dto.response.AuthResponse;
import com.example.SBA_M.entity.Account;
import com.example.SBA_M.entity.Role;
import com.example.SBA_M.entity.Token;
import com.example.SBA_M.exception.AppException;
import com.example.SBA_M.exception.ErrorCode;
import com.example.SBA_M.mapper.AccountMapper;
import com.example.SBA_M.repository.AccountRepository;
import com.example.SBA_M.repository.RoleRepository;
import com.example.SBA_M.repository.TokenRepository;
import com.example.SBA_M.utils.AccountStatus;
import com.example.SBA_M.utils.RoleName;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class AuthService {

    AccountRepository accountRepository;
    RoleRepository roleRepository;
    TokenRepository tokenRepository;
    PasswordEncoder passwordEncoder;
    JwtService jwtService;
    MailService mailService;

    @Value("${app.activation-code.expiration-minutes}")
    private long activationCodeExpirationMinutes;

    @Value("${app.password-reset-code.expiration-minutes}")
    private long passwordResetCodeExpirationMinutes;

    public void registerUser(AccountCreationRequest request) {
        if (accountRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }
        if (accountRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        Account account = AccountMapper.INSTANCE.toAccount(request);
        account.setPassword(passwordEncoder.encode(request.getPassword()));

        // FIX 1: Convert RoleName enum to String
        Role defaultRole = roleRepository.findByName(RoleName.USER.name())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        Set<Role> roles = new HashSet<>();
        roles.add(defaultRole);
        account.setRoles(roles);
        account.setStatus(AccountStatus.INACTIVE);
        account.setIsDeleted(false);

        String activationCode = UUID.randomUUID().toString();
        account.setCreatedBy(activationCode);
        account.setCreatedAt(Instant.now());

        accountRepository.save(account);

        String activationLink = "http://localhost:8080/api/v1/auth/activate?email=" + request.getEmail() + "&code=" + activationCode;
        mailService.sendActivationEmail(request.getEmail(), activationLink);
    }

    public void activateAccount(String email, String code) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

        if (account.getStatus() == AccountStatus.ACTIVE) {
            throw new AppException(ErrorCode.ACCOUNT_ALREADY_ACTIVE);
        }

        if (!code.equals(account.getCreatedBy())) {
            throw new AppException(ErrorCode.INVALID_ACTIVATION_CODE);
        }

        if (account.getCreatedAt().plus(activationCodeExpirationMinutes, ChronoUnit.MINUTES).isBefore(Instant.now())) {
            throw new AppException(ErrorCode.ACTIVATION_CODE_EXPIRED);
        }

        account.setStatus(AccountStatus.ACTIVE);
        account.setCreatedBy(null);
        account.setCreatedAt(null);
        accountRepository.save(account);
    }

    public AuthResponse authenticate(LoginRequest request) {
        // FIX 2: Assuming LoginRequest has getUsername() method
        Account account = accountRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new AppException(ErrorCode.ACCOUNT_NOT_ACTIVE);
        }

        account.setLastLoginAt(Instant.now());
        account.setLoginCount(account.getLoginCount() + 1);
        accountRepository.save(account);

        String accessToken = jwtService.generateAccessToken(account);
        String refreshToken = jwtService.generateRefreshToken(account);

        Token newToken = new Token();
        newToken.setAccount(account);
        newToken.setToken(refreshToken);
        newToken.setExpiresAt(Instant.now().plusMillis(jwtService.getRefreshTokenExpiration()));
        newToken.setRevoked(false);
        newToken.setCreatedAt(Instant.now());
        tokenRepository.save(newToken);

        // FIX 3: AuthResponse needs @Builder
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String refreshTokenString = request.getRefreshToken();
        // FIX 4: findByToken method must exist in TokenRepository
        Token storedToken = tokenRepository.findByToken(refreshTokenString)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_TOKEN));

        if (storedToken.getRevoked() || storedToken.getExpiresAt().isBefore(Instant.now())) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        Account account = storedToken.getAccount();
        if (account == null) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        String newAccessToken = jwtService.generateAccessToken(account);

        storedToken.setRevoked(true);
        tokenRepository.save(storedToken);

        String newRefreshToken = jwtService.generateRefreshToken(account);
        Token newToken = new Token();
        newToken.setAccount(account);
        newToken.setToken(newRefreshToken);
        newToken.setExpiresAt(Instant.now().plusMillis(jwtService.getRefreshTokenExpiration()));
        newToken.setRevoked(false);
        newToken.setCreatedAt(Instant.now());
        tokenRepository.save(newToken);

        // FIX 3 (again): AuthResponse needs @Builder
        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    public void requestPasswordReset(String email) {
        Account account = accountRepository.findByEmail(email)
                .orElse(null);

        if (account != null) {
            String resetCode = UUID.randomUUID().toString();
            account.setCreatedBy(resetCode);
            account.setCreatedAt(Instant.now());
            accountRepository.save(account);

            String resetLink = "http://localhost:8080/api/v1/auth/reset-password?email=" + email + "&token=" + resetCode;
            mailService.sendPasswordResetEmail(email, resetLink);
        }
    }

    public void resetPassword(String email, String token, String newPassword) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

        if (!token.equals(account.getCreatedBy())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD_RESET_TOKEN);
        }

        if (account.getCreatedAt().plus(passwordResetCodeExpirationMinutes, ChronoUnit.MINUTES).isBefore(Instant.now())) {
            throw new AppException(ErrorCode.PASSWORD_RESET_TOKEN_EXPIRED);
        }

        account.setPassword(passwordEncoder.encode(newPassword));
        account.setCreatedBy(null);
        account.setCreatedAt(null);
        accountRepository.save(account);
    }

    public void logout(String refreshToken) {
        // FIX 4 (again): findByToken method must exist in TokenRepository
        Token tokenEntity = tokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_TOKEN));

        if (tokenEntity.getRevoked()) {
            throw new AppException(ErrorCode.TOKEN_ALREADY_REVOKED);
        }

        tokenEntity.setRevoked(true);
        tokenRepository.save(tokenEntity);
    }

    public boolean existByUsername(String username) {
        return accountRepository.findByUsername(username).isPresent();
    }

    public boolean existByEmail(String email) {
        return accountRepository.findByEmail(email).isPresent();
    }

    public boolean verifyAccessToken(String token) {
        try {
            // FIX 5: extractAllClaims must be public in JwtService
            jwtService.extractAllClaims(token);
            return !jwtService.isTokenExpired(token);
        } catch (Exception e) {
            log.error("Token verification failed: {}", e.getMessage());
            return false;
        }
    }

    public void resendActivationEmail(String email) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

        if (account.getStatus() == AccountStatus.ACTIVE) {
            throw new AppException(ErrorCode.ACCOUNT_ALREADY_ACTIVE);
        }

        String newActivationCode = UUID.randomUUID().toString();
        account.setCreatedBy(newActivationCode);
        account.setCreatedAt(Instant.now());
        accountRepository.save(account);

        String activationLink = "http://localhost:8080/api/v1/auth/activate?email=" + email + "&code=" + newActivationCode;
        mailService.sendActivationEmail(email, activationLink);
    }
}