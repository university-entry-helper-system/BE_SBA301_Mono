package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.*;
import com.example.SBA_M.dto.response.ApiResponse;
import com.example.SBA_M.dto.response.AuthResponse;
import com.example.SBA_M.service.auth.AuthService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
    public ApiResponse<Void> register(@RequestBody @Valid AccountCreationRequest request) {
        authService.registerUser(request);
        return ApiResponse.<Void>builder()
                .code(1001)
                .message("User registered successfully. Please check your email for account activation.")
                .build();
    }

    @Operation(summary = "Activate account", description = "Activate user by email and code")
    @GetMapping("/activate")
    public ApiResponse<Void> activateAccount(@RequestParam String email, @RequestParam String code) {
        authService.activateAccount(email, code);
        return ApiResponse.<Void>builder()
                .code(1002)
                .message("Account activated successfully.")
                .build();
    }

    @Operation(summary = "User login", description = "Authenticate and get tokens")
    @RateLimiter(name = "authLimiter")
    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        AuthResponse response = authService.authenticate(request);
        return ApiResponse.<AuthResponse>builder()
                .code(1000)
                .message("Login successful")
                .result(response)
                .build();
    }

    @Operation(summary = "Refresh token", description = "Refresh access token using refresh token")
    @PostMapping("/refresh-token")
    public ApiResponse<AuthResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        AuthResponse response = authService.refreshToken(request);
        return ApiResponse.<AuthResponse>builder()
                .code(1000)
                .message("Token refreshed successfully")
                .result(response)
                .build();
    }

    @Operation(summary = "Forgot password", description = "Send password reset email")
    @RateLimiter(name = "authLimiter")
    @PostMapping("/forgot-password")
    public ApiResponse<Void> forgotPassword(@RequestParam String email) {
        authService.requestPasswordReset(email);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("If the email is registered, a password reset link has been sent to your inbox.")
                .build();
    }

    @Operation(summary = "Reset password", description = "Reset password using email and token")
    @RateLimiter(name = "authLimiter")
    @PostMapping("/reset-password")
    public ApiResponse<Void> resetPassword(@RequestParam String email, @RequestParam String token,
                                           @RequestBody @Valid PasswordCreationRequest request) {
        authService.resetPassword(email, token, request.getPassword());
        return ApiResponse.<Void>builder()
                .code(1002)
                .message("Password reset successfully. You can now log in with your new password.")
                .build();
    }

    @Operation(summary = "Logout", description = "Invalidate refresh token")
    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody @Valid RefreshTokenRequest request) {
        authService.logout(request.getRefreshToken());
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Logged out successfully.")
                .build();
    }

    @Operation(summary = "Check username existence", description = "Check if a username exists")
    @GetMapping("/check-username")
    public ApiResponse<Boolean> checkUsernameExists(@RequestParam String username) {
        boolean exists = authService.existByUsername(username);
        return ApiResponse.<Boolean>builder()
                .code(1000)
                .message("Username check complete")
                .result(exists)
                .build();
    }

    @Operation(summary = "Check email existence", description = "Check if an email exists")
    @GetMapping("/check-email")
    public ApiResponse<Boolean> checkEmailExists(@RequestParam String email) {
        boolean exists = authService.existByEmail(email);
        return ApiResponse.<Boolean>builder()
                .code(1000)
                .message("Email check complete")
                .result(exists)
                .build();
    }

    @Operation(summary = "Resend activation email", description = "Resend activation email to user")
    @RateLimiter(name = "authLimiter")
    @PostMapping("/resend-activation")
    public ApiResponse<Void> resendActivation(@RequestParam String email) {
        authService.resendActivationEmail(email);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Activation email resent successfully.")
                .build();
    }

    @Operation(summary = "Verify access token", description = "Validate access token string")
    @GetMapping("/verify-token")
    public ApiResponse<Boolean> verifyAccessToken(@RequestParam String token) {
        boolean isValid = authService.verifyAccessToken(token);
        return ApiResponse.<Boolean>builder()
                .code(1000)
                .message("Access token verification completed")
                .result(isValid)
                .build();
    }
}
