package com.example.SBA_M.service.auth;

import com.example.SBA_M.dto.request.AccountCreationRequest;
import com.example.SBA_M.dto.request.LoginRequest;
import com.example.SBA_M.dto.request.RefreshTokenRequest;
import com.example.SBA_M.dto.response.AuthResponse;
import com.example.SBA_M.dto.response.AccountResponse;
import com.example.SBA_M.entity.commands.Account;
import com.example.SBA_M.entity.commands.Role;
import com.example.SBA_M.entity.Token;
import com.example.SBA_M.exception.AppException;
import com.example.SBA_M.exception.ErrorCode;
import com.example.SBA_M.mapper.AccountMapper;
import com.example.SBA_M.repository.commands.AccountRepository;
import com.example.SBA_M.repository.commands.RoleRepository;
import com.example.SBA_M.repository.commands.TokenRepository;
import com.example.SBA_M.service.PageVisitService;
import com.example.SBA_M.utils.AccountStatus;
import com.example.SBA_M.utils.RoleName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final MailService mailService;
    private final AccountMapper accountMapper;
    private final PageVisitService pageVisitService;

    @Value("${app.activation-code.expiration-minutes}")
    private long activationCodeExpirationMinutes;

    @Value("${app.password-reset-code.expiration-minutes}")
    private long passwordResetCodeExpirationMinutes;

    @Transactional
    public AccountResponse registerUser(AccountCreationRequest request) {
        log.info("Starting user registration for username: {}", request.getUsername());
        
        // Validate username
        if (accountRepository.existsByUsername(request.getUsername())) {
            log.warn("Registration failed: Username '{}' already exists", request.getUsername());
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }

        // Validate email
        if (accountRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration failed: Email '{}' already exists", request.getEmail());
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        // Validate phone if provided
        if (request.getPhone() != null && !request.getPhone().trim().isEmpty()) {
            String normalizedPhone = request.getPhone().startsWith("+84") 
                ? "0" + request.getPhone().substring(3) 
                : request.getPhone();
            
            if (!normalizedPhone.matches("^0\\d{9}$")) {
                log.warn("Registration failed: Invalid phone number format '{}'", request.getPhone());
                throw new AppException(ErrorCode.INVALID_PARAM);
            }
            
            if (accountRepository.existsByPhone(normalizedPhone)) {
                log.warn("Registration failed: Phone number '{}' already exists", normalizedPhone);
                throw new AppException(ErrorCode.PHONE_EXISTED);
            }
            request.setPhone(normalizedPhone);
        }

        // Validate date of birth if provided
        if (request.getDob() != null && !request.getDob().trim().isEmpty()) {
            try {
                LocalDate dob = request.getDobAsLocalDate();
                if (dob != null) {
                    int year = dob.getYear();
                    int currentYear = LocalDate.now().getYear();
                    if (year < 1900 || year > currentYear) {
                        log.warn("Registration failed: Invalid birth year '{}'", year);
                        throw new AppException(ErrorCode.INVALID_PARAM);
                    }
                }
            } catch (DateTimeParseException e) {
                log.warn("Registration failed: Invalid date format for dob '{}'", request.getDob());
                throw new AppException(ErrorCode.INVALID_PARAM);
            } catch (IllegalArgumentException e) {
                log.warn("Registration failed: Invalid date value for dob '{}'", request.getDob());
                throw new AppException(ErrorCode.INVALID_PARAM);
            }
        }

        // Validate gender if provided
        if (request.getGender() != null) {
            // Đã là enum, không cần kiểm tra thêm
        }

        try {
            // Create new account
            Account account = accountMapper.toAccount(request);
            account.setPassword(passwordEncoder.encode(request.getPassword()));
            
            // Set default role
            Role userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
            
            // Create roles set with initial role
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            account.setRoles(roles);
            
            // Save account
            account = accountRepository.save(account);
            log.info("User registered successfully: {}", account.getUsername());
            
            // Generate activation code and send email
            String activationCode = UUID.randomUUID().toString();
            account.setCreatedBy(activationCode);
            account.setCreatedAt(Instant.now());
            accountRepository.save(account);

            String activationLink = "http://localhost:8080/api/v1/auth/activate?email=" + account.getEmail() + "&code=" + activationCode;
            mailService.sendActivationEmail(account.getEmail(), activationLink);
            
            return accountMapper.toAccountResponse(account);
        } catch (Exception e) {
            log.error("Registration failed with unexpected error: ", e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
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
        if(!account.getStatus().name().equals("ADMIN")) {
            pageVisitService.recordVisit();
        }



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