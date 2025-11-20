package org.example.bookvexebej2e.controllers.seat;

import java.util.UUID;

import org.example.bookvexebej2e.models.dto.seat.SeatHoldRequest;
import org.example.bookvexebej2e.services.external.WebSocketService;
import org.example.bookvexebej2e.services.seat.SeatHoldService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SeatWebSocketController {

    private final SeatHoldService seatHoldService;
    private final WebSocketService webSocketService;

    @MessageMapping("/seat/hold")
    public void holdSeats(@Payload SeatHoldRequest request, SimpMessageHeaderAccessor headerAccessor) {
        try {
            String sessionId = headerAccessor.getSessionId();
            UUID userId = getCurrentUserId(headerAccessor);

            log.info("Received seat hold request: tripId={}, carId={}, seats={}, sessionId={}, userId={}",
                    request.getTripId(), request.getCarId(), request.getSeatIds(), sessionId, userId);

            boolean success = seatHoldService.holdSeats(request, sessionId, userId);

            if (!success) {
                log.warn("Failed to hold seats for request: {}", request);
                // Send error message to the user
                if (userId != null) {
                    webSocketService.notifyUser(userId, "SEAT_HOLD_FAILED");
                }
            }

        } catch (Exception e) {
            log.error("Error processing seat hold request: {}", e.getMessage(), e);
        }
    }

    @MessageMapping("/seat/release")
    public void releaseSeats(@Payload SeatHoldRequest request, SimpMessageHeaderAccessor headerAccessor) {
        try {
            String sessionId = headerAccessor.getSessionId();
            UUID userId = getCurrentUserId(headerAccessor);

            log.info("Received seat release request: tripId={}, carId={}, seats={}, sessionId={}, userId={}",
                    request.getTripId(), request.getCarId(), request.getSeatIds(), sessionId, userId);

            boolean success = seatHoldService.releaseSeats(request, sessionId, userId);

            if (!success) {
                log.warn("Failed to release seats for request: {}", request);
            }

        } catch (Exception e) {
            log.error("Error processing seat release request: {}", e.getMessage(), e);
        }
    }

    private UUID getCurrentUserId(SimpMessageHeaderAccessor headerAccessor) {
        try {
            Authentication auth = (Authentication) headerAccessor.getUser();
            if (auth != null && auth.isAuthenticated()) {
                // Assuming the principal contains user ID or username
                String principal = auth.getName();
                // If principal is UUID string, convert it
                try {
                    return UUID.fromString(principal);
                } catch (IllegalArgumentException e) {
                    // If not UUID, could look up user by username
                    log.debug("Principal is not UUID: {}", principal);
                }
            }
        } catch (Exception e) {
            log.debug("Could not extract user ID from session: {}", e.getMessage());
        }
        return null;
    }
}