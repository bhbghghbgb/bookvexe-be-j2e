package com.bookvexe.notificationservice.services;

import com.bookvexe.notificationservice.entities.NotificationDbModel;
import com.bookvexe.notificationservice.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<NotificationDbModel> getNotificationsByUserId(UUID userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Long getUnreadCount(UUID userId) {
        return notificationRepository.countUnreadByUserId(userId);
    }

    public boolean markAsRead(UUID notificationId, UUID userId) {
        Optional<NotificationDbModel> notificationOpt = notificationRepository.findById(notificationId);
        if (notificationOpt.isPresent()) {
            NotificationDbModel notification = notificationOpt.get();
            // Kiểm tra xem notification có thuộc về user này không
            if (notification.getUser().equals(userId)) {
                notification.setIsRead(true);
                notificationRepository.save(notification);
                return true;
            }
        }
        return false;
    }

    public boolean deleteNotification(UUID notificationId, UUID userId) {
        Optional<NotificationDbModel> notificationOpt = notificationRepository.findById(notificationId);
        if (notificationOpt.isPresent()) {
            NotificationDbModel notification = notificationOpt.get();
            // Kiểm tra xem notification có thuộc về user này không
            if (notification.getUser().equals(userId)) {
                notificationRepository.delete(notification);
                return true;
            }
        }
        return false;
    }
}
