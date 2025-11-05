package com.bookvexe.notificationservice.controllers;

import com.bookvexe.notificationservice.entities.Notification;
import com.bookvexe.notificationservice.services.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notification-service-api")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*") // Cần cấu hình lại cho môi trường production
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/notifications")
    public ResponseEntity<List<Notification>> getNotifications(@RequestParam Long userId) {
        List<Notification> notifications = notificationService.getNotificationsByUserId(userId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/notifications/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(@RequestParam Long userId) {
        Long count = notificationService.getUnreadCount(userId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @PostMapping("/notifications/{id}/read")
    public ResponseEntity<Map<String, String>> markAsRead(
            @PathVariable Long id, 
            @RequestParam Long userId) {
        boolean success = notificationService.markAsRead(id, userId);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "Notification marked as read"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to mark notification as read"));
        }
    }

    @DeleteMapping("/notifications/{id}")
    public ResponseEntity<Map<String, String>> deleteNotification(
            @PathVariable Long id, 
            @RequestParam Long userId) {
        boolean success = notificationService.deleteNotification(id, userId);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "Notification deleted successfully"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to delete notification"));
        }
    }
}
