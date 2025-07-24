package com.example.SBA_M.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                // Allow connection establishment
                .simpDestMatchers("/app/consultation/subscribe").authenticated()

                // Consultant-specific destinations
                .simpDestMatchers("/topic/consultant/**").hasAnyRole("CONSULTANT", "ADMIN")
                .simpSubscribeDestMatchers("/topic/consultant/**").hasAnyRole("CONSULTANT", "ADMIN")

                // User-specific destinations
                .simpDestMatchers("/topic/user/**").hasAnyRole("USER", "ADMIN")
                .simpSubscribeDestMatchers("/topic/user/**").hasAnyRole("USER", "ADMIN")

                // General stats topic - allow for all authenticated users
                .simpDestMatchers("/topic/consultation/stats").authenticated()
                .simpSubscribeDestMatchers("/topic/consultation/stats").authenticated()

                // User-specific acknowledgment topic
                .simpSubscribeDestMatchers("/user/topic/consultation/ack").authenticated()

                // Require authentication for any other message
                .anyMessage().authenticated();
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true; // Allow cross-origin for development; set to false for production
    }
}