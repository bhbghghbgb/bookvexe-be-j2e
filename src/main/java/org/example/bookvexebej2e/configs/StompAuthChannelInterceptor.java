package org.example.bookvexebej2e.configs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompAuthChannelInterceptor implements ChannelInterceptor {

    private final JwtAuthenticationProvider jwtAuthenticationProvider; // Renamed for clarity

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authorizationHeader = accessor.getFirstNativeHeader("Authorization");

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Authentication authentication = jwtAuthenticationProvider.getAuthentication(token);

                if (authentication != null && authentication.getPrincipal() instanceof AuthUserDetails) {
                    // Create a simple Principal with just the username
                    String username = authentication.getName();
                    StompPrincipal principal = new StompPrincipal(username);
                    accessor.setUser(principal);

                    log.info("WebSocket: User {} authenticated via STOMP CONNECT. Principal name: {}",
                        username, principal.getName());
                } else {
                    log.warn("WebSocket: Authentication failed or token invalid/revoked.");
                    accessor.setUser(null);
                }
            } else {
                log.info("WebSocket: CONNECT without Authorization header.");
            }
        } else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            Principal user = accessor.getUser();
            log.info("WebSocket SUBSCRIBE: destination={}, user={}, session={}",
                accessor.getDestination(),
                user != null ? user.getName() : "null",
                accessor.getSessionId());
        }

        return message;
    }
}