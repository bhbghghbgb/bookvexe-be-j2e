package org.example.bookvexebej2e.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookvexebej2e.models.dto.notification.NotificationResponse;
import org.example.bookvexebej2e.models.dto.user.UserResponse;
import org.example.bookvexebej2e.services.notification.NotificationService;
import org.example.bookvexebej2e.services.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final NotificationService notificationService;
    private final UserService userService;

    @GetMapping("/hello")
    public Map<String, String> hello() {
        return Map.of("message", "Hello World");
    }

    @PostMapping("/test-notification")
    public Map<String, Object> testNotificationUnified(@RequestParam(required = false) UUID userId, // Target user ID
        // (optional)
        @RequestParam(required = false) String toEmail, // Email override (optional)
        @RequestParam(required = false, defaultValue = "TEST_NOTIFICATION") String typeCode, @RequestParam(required =
            false, defaultValue = "false") Boolean sendEmail, @RequestParam(required = false) String title,
        @RequestParam(required = false) String message, @RequestParam(required = false) UUID bookingId,
        @RequestParam(required = false) UUID tripId,
        @RequestParam(required = false, defaultValue = "CHANNEL_TEST") String channel, @RequestParam(required = false
            , defaultValue = "false") Boolean shouldSave, // Control persistence
        Authentication authentication) {

        UUID authenticatedUserId = getAuthenticatedUserId(authentication);
        // 1. Determine the target user ID for WebSocket ping/ownership
        UUID targetUserId = userId != null ? userId : authenticatedUserId;

        // 2. Prepare content
        String notificationTitle = title != null ? title : "Test Notification - " + typeCode;
        String notificationMessage = message != null ? message : "Notification Type: " + typeCode + (toEmail != null
            ? " | Email Override: " + toEmail : "") + " | Time: " + LocalDateTime.now();

        // 3. Determine which method to call
        try {
            NotificationResponse response;
            String emailDetail = "N/A";

            if (Boolean.TRUE.equals(sendEmail) && toEmail != null && !toEmail.isBlank()) {
                response = notificationService.sendNotification(targetUserId, toEmail, // Use the explicit email
                    typeCode, notificationTitle, notificationMessage, bookingId, tripId, channel, true,
                    shouldSave);
                emailDetail = "Sent to Override: " + toEmail;

            } else {
                response = notificationService.sendNotification(targetUserId, typeCode, notificationTitle,
                    notificationMessage, bookingId, tripId, channel, sendEmail, // Use the parameter value
                    shouldSave);
                emailDetail = Boolean.TRUE.equals(sendEmail) ? "Sent via User Lookup" : "Email disabled";
            }

            return Map.of("status", "success", "message", "Notification sent successfully", "notification", response,
                "details",
                Map.of("targetUserId", targetUserId, "typeCode", typeCode, "sendEmailMode", emailDetail, "savedToDB",
                    shouldSave));

        } catch (Exception e) {
            log.error("Failed to send notification for user {}: {}", targetUserId, e.getMessage(), e);
            return Map.of("status", "error", "message", "Failed to send notification: " + e.getMessage());
        }
    }

    // Helper method to extract user ID from authentication
    private UUID getAuthenticatedUserId(Authentication authentication) {
        // If you have a method in userService to get current user ID, use it
        // Otherwise, extract from authentication principal
        UserResponse currentUser = userService.getCurrentUser();
        return currentUser.getId();
    }
}
