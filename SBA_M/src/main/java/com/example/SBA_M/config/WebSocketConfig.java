package com.example.SBA_M.config;

import com.example.SBA_M.service.auth.JwtService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtService jwtService;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable simple message broker for topics and user-specific destinations
        config.enableSimpleBroker("/topic", "/user");
        // Set application destination prefix
        config.setApplicationDestinationPrefixes("/app");
        // Set user destination prefix
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws-consultations")
                .setAllowedOriginPatterns("*") // Configure properly for production
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
                    // Extract token from Authorization header
                    String authHeader = accessor.getFirstNativeHeader("Authorization");
                    String token = null;

                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        token = authHeader.substring(7);
                    } else {
                        // Fallback: try to get token from query parameters (SockJS compatibility)
                        List<String> tokenParams = accessor.getNativeHeader("token");
                        if (tokenParams != null && !tokenParams.isEmpty()) {
                            token = tokenParams.get(0);
                        }
                    }

                    if (token != null && !token.trim().isEmpty()) {
                        try {
                            // Validate JWT token using your JwtService
                            String username = jwtService.extractUsername(token);

                            if (username != null && !jwtService.isTokenExpired(token)) {
                                // Extract claims for user information
                                Claims claims = jwtService.extractAllClaims(token);
                                String roleName = claims.get("roleName", String.class);

                                // Create authorities
                                List<SimpleGrantedAuthority> authorities = Collections.emptyList();
                                if (roleName != null) {
                                    // Handle role names - your JWT already includes "ROLE_" prefix
                                    authorities = List.of(new SimpleGrantedAuthority(roleName));
                                }

                                // Create authentication object
                                Authentication auth = new UsernamePasswordAuthenticationToken(
                                        username, null, authorities
                                );

                                accessor.setUser(auth);
                                log.info("WebSocket authentication successful for user: {} with role: {}",
                                        username, roleName);
                            } else {
                                log.error("WebSocket authentication failed: Token expired or invalid username");
                                throw new SecurityException("Invalid or expired token");
                            }
                        } catch (Exception e) {
                            log.error("WebSocket authentication error: {}", e.getMessage());
                            throw new SecurityException("Authentication failed: " + e.getMessage());
                        }
                    } else {
                        log.error("WebSocket authentication failed: Missing Authorization token");
                        throw new SecurityException("Missing authentication token");
                    }
                }

                return message;
            }
        });
    }
}