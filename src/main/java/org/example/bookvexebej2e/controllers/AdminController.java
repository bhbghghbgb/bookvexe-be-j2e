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

    /**
     * Primary test route: Sends a notification to the authenticated user.
     * TypeCode is defaulted to "TEST_NOTIFICATION".
     */
    @PostMapping("/test-notification")
    public Map<String, Object> testNotification(
        @RequestParam(required = false, defaultValue = "TEST_NOTIFICATION") String typeCode,
        @RequestParam(required = false, defaultValue = "false") Boolean sendEmail,
        @RequestParam(required = false) String title,
        @RequestParam(required = false) String message,
        @RequestParam(required = false) UUID bookingId,
        @RequestParam(required = false) UUID tripId,
        @RequestParam(required = false, defaultValue = "CHANNEL_TEST") String channel,
        Authentication authentication) {

        try {
            UUID userId = getAuthenticatedUserId(authentication);

            String notificationTitle = title != null ? title : "Test Notification - " + typeCode;
            String notificationMessage = message != null ? message :
                "This is a test notification of type: " + typeCode + " sent at: " + LocalDateTime.now();

            NotificationResponse response = notificationService.sendNotification(
                userId,
                typeCode,
                notificationTitle,
                notificationMessage,
                bookingId,
                tripId,
                channel,
                sendEmail,
                false // shouldSave = false for testing
            );

            return Map.of("status", "success", "message", "Notification sent successfully", "notification", response,
                "details", Map.of("userId", userId, "typeCode", typeCode, "sendEmail", sendEmail, "saved", false));

        } catch (Exception e) {
            return Map.of("status", "error", "message", "Failed to send notification: " + e.getMessage());
        }
    }

    /**
     * Sends a notification to the authenticated user's WebSocket,
     * but sends the email to a specified override address (useful for Admin testing).
     */
    @PostMapping("/test-notification-email-override")
    public Map<String, Object> testNotificationEmailOverride(
        @RequestParam String toEmail, // Require the email override
        @RequestParam(required = false, defaultValue = "TEST_NOTIFICATION") String typeCode,
        @RequestParam(required = false, defaultValue = "true") Boolean sendEmail, // Default email to true
        @RequestParam(required = false) String title,
        @RequestParam(required = false) String message,
        Authentication authentication) {

        try {
            // Use authenticated user ID for WebSocket ping/DB record ownership
            UUID userId = getAuthenticatedUserId(authentication);

            String notificationTitle = title != null ? title : "Override Test - " + typeCode;
            String notificationMessage = message != null ? message :
                "This is an email test sent to: " + toEmail + " at: " + LocalDateTime.now();

            NotificationResponse response = notificationService.sendNotification(
                userId,
                toEmail, // The email override parameter
                typeCode,
                notificationTitle,
                notificationMessage,
                null, // bookingId
                null, // tripId
                "CHANNEL_EMAIL_TEST",
                sendEmail,
                false // shouldSave = false
            );

            return Map.of("status", "success",
                "message", "Notification sent successfully. Email sent to: " + toEmail,
                "notification", response);

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
}
