package com.example.SBA_M.config;

import com.example.SBA_M.security.CustomUserDetailsService;
import com.example.SBA_M.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // Enables @PreAuthorize
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Uses BCrypt algorithm
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity // Enables Spring Security
@EnableMethodSecurity(prePostEnabled = true) // Enables @PreAuthorize for method-level security
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthFilter; // Custom JWT filter

    // 1. Configure PasswordEncoder using BCrypt (recommended hashing algorithm)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Configure AuthenticationProvider
    // Uses DaoAuthenticationProvider to load user details from the custom UserDetailsService
    // and validates passwords with the configured PasswordEncoder
    @Bean
    @SuppressWarnings("deprecation")
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // 3. Configure AuthenticationManager
    // Used by authentication endpoints such as /auth/login
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // 4. Configure SecurityFilterChain
    // Defines security rules for incoming HTTP requests
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF as we're using stateless JWT authentication
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable CORS with custom config
            .authorizeHttpRequests(auth -> auth
                // Public endpoints (no authentication required)
                .requestMatchers(
                        "/api/v1/auth/**",
                        "/api/v1/accounts/check-username",
                        "/api/v1/accounts/check-email",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/api/v1/universities/**",
                        "/api/v1/university-categories/**",
                        "/api/v1/news/**",
                        "/api/v1/exam-subjects/**",
                        "/api/v1/university-majors/**",
                        "/api/v1/university-admission-methods/**",
                        "api/v1/provinces/**",
                        "/api/v1/admission-methods/**",
                        "api/v1/graduation-score/**",
                        "api/v1/faqs/**"
                ).permitAll()
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Disable session creation; use stateless JWT
            )
            .authenticationProvider(authenticationProvider()) // Register custom AuthenticationProvider
            // Add JWT filter before the default UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 5. Configure CORS settings
    // Important for allowing frontend apps on different ports/domains to call the API
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173")); // Replace with your frontend origin
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*")); // Allow all headers
        configuration.setAllowCredentials(true); // Allow credentials like cookies (for refresh tokens, etc.)
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply CORS config to all routes
        return source;
    }
}
