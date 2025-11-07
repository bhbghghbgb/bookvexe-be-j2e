package org.example.bookvexebej2e.controllers;


import lombok.RequiredArgsConstructor;
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
public class AdminController {

    private final NotificationService notificationService;
    private final UserService userService;

    @GetMapping("/hello")
    public Map<String, String> hello() {
        return Map.of("message", "Hello World");
    }


    @PostMapping("/test-notification")
    public Map<String, Object> testNotification(@RequestParam String typeCode, @RequestParam(required = false,
        defaultValue = "false") Boolean sendEmail, @RequestParam(required = false) String title,
        @RequestParam(required = false) String message, @RequestParam(required = false) UUID bookingId,
        @RequestParam(required = false) UUID tripId,
        @RequestParam(required = false, defaultValue = "CHANNEL_TEST") String channel, Authentication authentication) {

        try {
            // Get current user ID from authentication
            UUID userId = getAuthenticatedUserId(authentication);

            // Set default title and message if not provided
            String notificationTitle = title != null ? title : "Test Notification - " + typeCode;
            String notificationMessage = message != null ? message :
                "This is a test notification of type: " + typeCode + " sent at: " + LocalDateTime.now();

            // Call notification service
            NotificationResponse response = notificationService.sendNotification(userId, typeCode, notificationTitle,
                notificationMessage, bookingId,  // can be null
                tripId,     // can be null
                channel, sendEmail, false       // shouldSave = false for testing
            );

            return Map.of("status", "success", "message", "Notification sent successfully", "notification", response,
                "details", Map.of("userId", userId, "typeCode", typeCode, "sendEmail", sendEmail, "saved", false));

        } catch (Exception e) {
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

    // Alternative version if you prefer using the authentication directly
    @PostMapping("/test-notification-v2")
    public Map<String, Object> testNotificationV2(@RequestParam String typeCode, @RequestParam(required = false,
        defaultValue = "false") Boolean sendEmail, @RequestParam(required = false) String title,
        @RequestParam(required = false) String message, @RequestParam(required = false) UUID bookingId,
        @RequestParam(required = false) UUID tripId,
        @RequestParam(required = false, defaultValue = "CHANNEL_TEST") String channel, Authentication authentication) {

        try {
            // Alternative way to get user ID
            UUID userId = getAuthenticatedUserId(authentication);

            String notificationTitle = title != null ? title : "Test Notification - " + typeCode;
            String notificationMessage = message != null ? message : "This is a test notification of type: " + typeCode;

            NotificationResponse response = notificationService.sendNotification(userId, typeCode, notificationTitle,
                notificationMessage, bookingId, tripId, channel, sendEmail, false);

            return Map.of("status", "success", "notification", response);

        } catch (Exception e) {
            return Map.of("status", "error", "message", e.getMessage());
        }
    }

    // Simple version with minimal parameters
    @PostMapping("/test-notification-simple")
    public Map<String, Object> testNotificationSimple(@RequestParam String typeCode, @RequestParam(required = false,
        defaultValue = "false") Boolean sendEmail, Authentication authentication) {

        UUID userId = getAuthenticatedUserId(authentication);

        NotificationResponse response = notificationService.sendNotification(userId, typeCode, "Test Notification",
            "This is a test notification sent at: " + LocalDateTime.now(), null, // bookingId
            null, // tripId
            "CHANNEL_TEST", sendEmail, false);

        return Map.of("status", "success", "notification", response);
    }
}
