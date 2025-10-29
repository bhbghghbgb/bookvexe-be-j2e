package org.example.bookvexebej2e.controllers.notification;

import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.configs.SecurityUtils;
import org.example.bookvexebej2e.exceptions.UnauthorizedException;
import org.example.bookvexebej2e.mappers.NotificationQueryMapper;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.models.dto.notification.NotificationQuery;
import org.example.bookvexebej2e.models.dto.notification.NotificationResponse;
import org.example.bookvexebej2e.models.dto.notification.NotificationUserQuery;
import org.example.bookvexebej2e.services.notification.NotificationService;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationUserController {

    private final NotificationService notificationService;
    private final SecurityUtils securityUtils;
    private final NotificationQueryMapper queryMapper;

    // Inject the admin controller to call its create method internally
    private final NotificationController notificationController;

    private UUID getCurrentUserId() {
        UserDbModel user = securityUtils.getCurrentUserEntity();
        if (user == null) {
            // Should be caught by Spring Security, but this is a fail-safe
            throw new UnauthorizedException("User not authenticated.");
        }
        return user.getId();
    }

    /**
     * GET /notifications
     * Customer views their own notifications.
     */
    @GetMapping
    public ResponseEntity<Page<NotificationResponse>> getMyNotifications(NotificationUserQuery userQuery) {
        UUID userId = getCurrentUserId();

        // Map the user-facing query and inject the authenticated userId
        NotificationQuery query = queryMapper.toNotificationQueryWithUser(userQuery, userId);

        Page<NotificationResponse> notifications = notificationService.getMyNotifications(userId, query);
        return ResponseEntity.ok(notifications);
    }

    /**
     * GET /notifications/unread-count
     * Customer views the count of unread notifications.
     */
    @GetMapping("/unread-count")
    public ResponseEntity<Integer> countUnreadNotifications() {
        UUID userId = getCurrentUserId();
        int count = notificationService.countUnreadNotifications(userId);
        return ResponseEntity.ok(count);
    }

    /**
     * PATCH /notifications/{id}/read
     * Customer marks a notification as read.
     */
    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable("id") UUID notificationId) {
        UUID userId = getCurrentUserId();
        notificationService.markNotificationAsRead(notificationId, userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * DELETE /notifications/{id}
     * Customer deletes a notification from their list (soft delete).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable("id") UUID notificationId) {
        UUID userId = getCurrentUserId();
        notificationService.deleteNotification(notificationId, userId);
        return ResponseEntity.noContent().build();
    }

    // NOTE: The sendNotification method is a high-level service function intended
    // for internal use by other modules (e.g., BookingService, TripService), NOT
    // a public REST endpoint, so it is not exposed here.

    /**
     * POST /notifications/test-create
     * INTERNAL/DEBUG endpoint to trigger an unsaved notification and WS ping.
     * Only available when the 'dev' profile is active.
     */
    @Profile("dev")
    @PostMapping("/test-create")
    public ResponseEntity<NotificationResponse> createDebugNotification() {
        UUID userId = getCurrentUserId();

        // Fixed values for testing
        String typeCode = "TEST_TYPE"; // Must exist in NotificationTypeDbModel
        String title = "WebSocket Test Ping";
        String message = "Message received at " + java.time.LocalDateTime.now() + " (User: " + userId + ")";

        // Call the enhanced service method with shouldSave = false
        NotificationResponse response = notificationService.sendNotification(
            userId,
            typeCode,
            title,
            message,
            null,       // bookingId
            null,       // tripId
            "TEST_WS",  // channel
            false,      // sendEmail (We only want the WS test)
            false       // Do NOT save to DB
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Fallback for POST /notifications/test-create when profile is not 'dev'.
     */
    @Profile("!dev")
    @PostMapping("/test-create")
    public ResponseEntity<Void> createDebugNotificationForbidden() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}