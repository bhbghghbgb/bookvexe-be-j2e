package org.example.bookvexebej2e.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Application destination prefix for messages handled by @MessageMapping in controllers
        config.setApplicationDestinationPrefixes("/app");
        // Simple broker for general topics and user-specific queues
        config.enableSimpleBroker("/topic", "/user");
        // Use the user's ID or session ID for user-specific queue handling
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Endpoint for the WebSocket handshake, secured with CORS
        registry.addEndpoint("/ws")
            .setAllowedOriginPatterns("*") // Adjust for production CORS
            .withSockJS(); // Use SockJS for fallback
    }
}