package com.example.SBA_M.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .simpDestMatchers("/topic/consultant/*").hasAnyRole("CONSULTANT", "ADMIN")
                .simpDestMatchers("/topic/user/*").hasAnyRole("USER", "ADMIN")
                .simpSubscribeDestMatchers("/topic/consultant/*").hasAnyRole("CONSULTANT", "ADMIN")
                .simpSubscribeDestMatchers("/topic/user/*").hasAnyRole("USER", "ADMIN")
                .anyMessage().authenticated();
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true; // Allow cross-origin for development; adjust for production
    }
}