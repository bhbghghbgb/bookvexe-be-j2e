package org.example.bookvexebej2e.services.external;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Sends a real-time event to a specific user's private queue.
     * The frontend will subscribe to /user/{userId}/queue/notifications
     */
    public void notifyUser(UUID userId, String eventType) {
        // The destination format is /user/{userId}/queue/notifications
        // Spring automatically resolves the /user prefix and sends to the connected user's session
        messagingTemplate.convertAndSendToUser(
            userId.toString(),
            "/queue/notifications",
            // We send a simple event type, prompting the frontend to refetch data
            eventType
        );
    }
}