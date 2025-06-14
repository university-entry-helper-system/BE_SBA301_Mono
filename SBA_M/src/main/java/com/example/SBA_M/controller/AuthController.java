package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.AccountCreationRequest;
import com.example.SBA_M.dto.request.LoginRequest;
import com.example.SBA_M.dto.request.PasswordCreationRequest;
import com.example.SBA_M.dto.request.RefreshTokenRequest;
import com.example.SBA_M.dto.response.AuthResponse;
import com.example.SBA_M.dto.response.MessageResponse;
import com.example.SBA_M.service.AuthService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
class AuthController {

    AuthService authService;

    @Operation(summary = "User registration", description = "Register a new account")
    @RateLimiter(name = "authLimiter")
    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@RequestBody @Valid AccountCreationRequest request) {
        authService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MessageResponse("User registered successfully. Please check your email for account activation."));
    }

    @Operation(summary = "Activate account", description = "Activate user by email and code")
    @GetMapping("/activate")
    public ResponseEntity<MessageResponse> activateAccount(@RequestParam String email, @RequestParam String code) {
        authService.activateAccount(email, code);
        return ResponseEntity.ok(new MessageResponse("Account activated successfully."));
    }

    @Operation(summary = "User login", description = "Authenticate and get tokens")
    @RateLimiter(name = "authLimiter")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @Operation(summary = "Refresh token", description = "Refresh access token using refresh token")
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @Operation(summary = "Forgot password", description = "Send password reset email")
    @RateLimiter(name = "authLimiter")
    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponse> forgotPassword(@RequestParam String email) {
        authService.requestPasswordReset(email);
        return ResponseEntity.ok(new MessageResponse("If the email is registered, a password reset link has been sent to your inbox."));
    }

    @Operation(summary = "Reset password", description = "Reset password using email and token")
    @RateLimiter(name = "authLimiter")
    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(@RequestParam String email, @RequestParam String token,
                                                         @RequestBody @Valid PasswordCreationRequest request) {
        authService.resetPassword(email, token, request.getPassword());
        return ResponseEntity.ok(new MessageResponse("Password reset successfully. You can now log in with your new password."));
    }

    @Operation(summary = "Logout", description = "Invalidate refresh token")
    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout(@RequestBody @Valid RefreshTokenRequest request) {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok(new MessageResponse("Logged out successfully."));
    }

    @Operation(summary = "Check username existence", description = "Check if a username exists")
    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsernameExists(@RequestParam String username) {
        return ResponseEntity.ok(authService.existByUsername(username));
    }

    @Operation(summary = "Check email existence", description = "Check if an email exists")
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        return ResponseEntity.ok(authService.existByEmail(email));
    }

    @Operation(summary = "Resend activation email", description = "Resend activation email to user")
    @RateLimiter(name = "authLimiter")
    @PostMapping("/resend-activation")
    public ResponseEntity<MessageResponse> resendActivation(@RequestParam String email) {
        authService.resendActivationEmail(email);
        return ResponseEntity.ok(new MessageResponse("Activation email resent successfully."));
    }

    @Operation(summary = "Verify access token", description = "Validate access token string")
    @GetMapping("/verify-token")
    public ResponseEntity<Boolean> verifyAccessToken(@RequestParam String token) {
        return ResponseEntity.ok(authService.verifyAccessToken(token));
    }
}