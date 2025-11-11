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
import java.util.HashMap;
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
    public Map<String, Object> testNotificationUnified(@RequestParam(required = false) UUID userId,
        @RequestParam(required = false) String toEmail, @RequestParam(required = false, defaultValue =
            "TEST_NOTIFICATION") String typeCode,
        @RequestParam(required = false, defaultValue = "false") Boolean sendEmail,
        @RequestParam(required = false) String title, @RequestParam(required = false) String message,
        @RequestParam(required = false) UUID bookingId, @RequestParam(required = false) UUID tripId,
        @RequestParam(required = false, defaultValue = "CHANNEL_TEST") String channel, @RequestParam(required = false
            , defaultValue = "false") Boolean shouldSave, Authentication authentication) {

        UUID authenticatedUserId = getAuthenticatedUserId(authentication);
        UUID targetUserId = userId != null ? userId : authenticatedUserId;

        String notificationTitle = title != null ? title : "Test Notification - " + typeCode;
        String notificationMessage = message != null ? message : "Notification Type: " + typeCode + (toEmail != null
            ? " | Email Override: " + toEmail : "") + " | Time: " + LocalDateTime.now();

        try {
            NotificationResponse response;
            String emailDetail = "N/A";
            String emailResolution = "not requested";
            String userType = "authenticated";

            // Handle guest scenario explicitly
            if (targetUserId == null) {
                userType = "guest";
                response = notificationService.sendGuestNotification(toEmail, typeCode, notificationTitle,
                    notificationMessage, bookingId, tripId, channel, sendEmail, shouldSave);
                emailDetail = Boolean.TRUE.equals(
                    sendEmail) ? (toEmail != null ? "Sent to: " + toEmail : "Attempted booking lookup") : "Email " +
                    "disabled";
                emailResolution = "guest_mode";
            }
            // Existing logic for authenticated users
            else if (Boolean.TRUE.equals(sendEmail)) {
                if (toEmail != null && !toEmail.isBlank()) {
                    response = notificationService.sendNotification(targetUserId, toEmail, typeCode, notificationTitle,
                        notificationMessage, bookingId, tripId, channel, true, shouldSave);
                    emailDetail = "Sent to Override: " + toEmail;
                    emailResolution = "explicit_email";
                } else {
                    response = notificationService.sendNotification(targetUserId, typeCode, notificationTitle,
                        notificationMessage, bookingId, tripId, channel, true, shouldSave);
                    emailDetail = "Resolved from available sources";
                    emailResolution = "auto_resolved";
                }
            } else {
                response = notificationService.sendNotification(targetUserId, typeCode, notificationTitle,
                    notificationMessage, bookingId, tripId, channel, false, shouldSave);
                emailDetail = "Email disabled";
                emailResolution = "disabled";
            }

            // FIX: Use HashMap instead of Map.of to handle null values
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "Notification sent successfully");
            result.put("notification", response);

            Map<String, Object> details = new HashMap<>();
            details.put("userType", userType);
            details.put("targetUserId", targetUserId); // This can be null now
            details.put("typeCode", typeCode);
            details.put("sendEmailMode", emailDetail);
            details.put("emailResolution", emailResolution);
            details.put("savedToDB", shouldSave);
            details.put("bookingId", bookingId); // This can also be null

            result.put("details", details);

            return result;

        } catch (Exception e) {
            log.error("Failed to send notification for user {}: {}", targetUserId, e.getMessage(), e);
            return Map.of("status", "error", "message", "Failed to send notification: " + e.getMessage());
        }
    }

    @PostMapping("/test-guest-notification")
    public Map<String, Object> testGuestNotification(@RequestParam(required = false) String toEmail,
        @RequestParam(required = false, defaultValue = "GUEST_NOTIFICATION") String typeCode, @RequestParam(required
            = false, defaultValue = "true") Boolean sendEmail, @RequestParam(required = false) String title,
        @RequestParam(required = false) String message, @RequestParam(required = false) UUID bookingId,
        @RequestParam(required = false) UUID tripId,
        @RequestParam(required = false, defaultValue = "EMAIL") String channel, @RequestParam(required = false,
            defaultValue = "false") Boolean shouldSave) {

        String notificationTitle = title != null ? title : "Guest Test Notification - " + typeCode;
        String notificationMessage = message != null ? message :
            "Guest Notification Type: " + typeCode + " | Time: " + LocalDateTime.now();

        try {
            NotificationResponse response = notificationService.sendGuestNotification(toEmail, typeCode,
                notificationTitle, notificationMessage, bookingId, tripId, channel, sendEmail, shouldSave);

            String emailDetail = Boolean.TRUE.equals(
                sendEmail) ? (toEmail != null ? "Sent to: " + toEmail : "Attempted booking lookup") : "Email disabled";

            // FIX: Use HashMap for guest endpoint too
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "Guest notification sent successfully");
            result.put("notification", response);

            Map<String, Object> details = new HashMap<>();
            details.put("userType", "guest");
            details.put("toEmail", toEmail != null ? toEmail : "not provided");
            details.put("typeCode", typeCode);
            details.put("sendEmailMode", emailDetail);
            details.put("savedToDB", shouldSave);
            details.put("bookingId", bookingId);
            details.put("note", "Guest notifications cannot be saved to DB or use WebSocket");

            result.put("details", details);

            return result;

        } catch (Exception e) {
            log.error("Failed to send guest notification: {}", e.getMessage(), e);
            return Map.of("status", "error", "message", "Failed to send guest notification: " + e.getMessage());
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
