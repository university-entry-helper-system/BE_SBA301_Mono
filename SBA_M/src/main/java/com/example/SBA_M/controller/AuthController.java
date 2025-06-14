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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
public class AuthController {

    AuthService authService; // Dịch vụ xác thực

    // Register a new user (student, general user)
    @Operation(summary = "Register a new user", description = "Allows a new user to register and sends an email for activation.")
    @ApiResponse(responseCode = "201", description = "User registration successful. Check email for activation.")
    @ApiResponse(responseCode = "400", description = "Bad request due to invalid input or existing user.")
    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@RequestBody @Valid AccountCreationRequest request) {
        authService.registerUser(request); // Service sẽ xử lý logic đăng ký và gửi email
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MessageResponse("User registered successfully. Please check your email for account activation."));
    }

    // Activate user account via email verification token
    @Operation(summary = "Activate user account", description = "Activates a user account using an activation code received via email.")
    @ApiResponse(responseCode = "200", description = "Account activated successfully.")
    @ApiResponse(responseCode = "400", description = "Activation failed due to invalid email or code.")
    @GetMapping("/activate")
    public ResponseEntity<MessageResponse> activateAccount(@RequestParam String email, @RequestParam String code) {
        authService.activateAccount(email, code);
        return ResponseEntity.ok(new MessageResponse("Account activated successfully. You can now log in."));
    }

    // User login
    @RateLimiter(name = "loginLimiter")
    @Operation(summary = "User login", description = "Authenticates a user and returns access and refresh tokens.")
    @ApiResponse(responseCode = "200", description = "Login successful. Tokens provided.")
    @ApiResponse(responseCode = "401", description = "Unauthorized due to invalid credentials.")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        AuthResponse response = authService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    // Refresh access token using refresh token
    @Operation(summary = "Refresh access token", description = "Uses a refresh token to obtain a new access token.")
    @ApiResponse(responseCode = "200", description = "Access token refreshed successfully.")
    @ApiResponse(responseCode = "401", description = "Unauthorized due to invalid or expired refresh token.")
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        AuthResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

    // Request password reset email
    @Operation(summary = "Request password reset", description = "Sends a password reset link to the user's email.")
    @ApiResponse(responseCode = "200", description = "Password reset email sent. Check your inbox.")
    @ApiResponse(responseCode = "404", description = "Email not found.")
    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponse> forgotPassword(@RequestParam String email) {
        authService.requestPasswordReset(email);
        return ResponseEntity.ok(new MessageResponse("If the email is registered, a password reset link has been sent to your inbox."));
    }

    // Reset password using a valid token
    @Operation(summary = "Reset password", description = "Allows a user to reset their password using a reset token.")
    @ApiResponse(responseCode = "200", description = "Password reset successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid reset token, email, or password.")
    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(@RequestParam String email, @RequestParam String token,
                                                         @RequestBody @Valid PasswordCreationRequest newPasswordRequest) {
        authService.resetPassword(email, token, newPasswordRequest.getPassword());
        return ResponseEntity.ok(new MessageResponse("Password reset successfully. You can now log in with your new password."));
    }

    // Logout user (revoke refresh token)
    @Operation(summary = "Logout user", description = "Revokes the provided refresh token, invalidating the session.")
    @ApiResponse(responseCode = "200", description = "Logged out successfully.")
    @ApiResponse(responseCode = "401", description = "Unauthorized or invalid token.")
    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout(@RequestBody @Valid RefreshTokenRequest request) {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok(new MessageResponse("Logged out successfully."));
    }

    // Check if username exists (for client-side validation during registration)
    @Operation(summary = "Check if username exists", description = "Checks if a given username already exists. Returns true/false.")
    @ApiResponse(responseCode = "200", description = "Existence check successful.")
    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsernameExists(@RequestParam String username) {
        Boolean result = authService.existByUsername(username);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Resend activation email", description = "Resends account activation email.")
    @ApiResponse(responseCode = "200", description = "Activation email sent again.")
    @PostMapping("/resend-activation")
    public ResponseEntity<MessageResponse> resendActivation(@RequestParam String email) {
        authService.resendActivationEmail(email);
        return ResponseEntity.ok(new MessageResponse("Activation email resent successfully."));
    }


    // Check if email exists (for client-side validation during registration)
    @Operation(summary = "Check if email exists", description = "Checks if a given email already exists. Returns true/false.")
    @ApiResponse(responseCode = "200", description = "Existence check successful.")
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        Boolean result = authService.existByEmail(email);
        return ResponseEntity.ok(result);
    }
}