package org.example.bookvexebej2e.configs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompAuthChannelInterceptor stompAuthChannelInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Application destination prefix for messages handled by @MessageMapping in controllers
        config.setApplicationDestinationPrefixes("/app");
        // Simple broker for general topics and user-specific queues
        config.enableSimpleBroker("/topic", "/queue");
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

    /**
     * Register the custom ChannelInterceptor on the client inbound channel.
     * This is where Spring processes the STOMP CONNECT frame headers.
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompAuthChannelInterceptor);
    }


    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        log.info("WebSocket CONNECTED: user={}, sessionId={}",
            sha.getUser() != null ? sha.getUser().getName() : "null",
            sha.getSessionId());
    }

    @EventListener
    public void handleSessionSubscribe(SessionSubscribeEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        log.info("WebSocket SUBSCRIBED: user={}, sessionId={}, destination={}",
            sha.getUser() != null ? sha.getUser().getName() : "null",
            sha.getSessionId(),
            sha.getDestination());
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        log.info("WebSocket DISCONNECTED: user={}, sessionId={}",
            sha.getUser() != null ? sha.getUser().getName() : "null",
            sha.getSessionId());
    }
}