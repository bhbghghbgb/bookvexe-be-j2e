package com.bookvexe.notificationservice.dto.notification;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class NotificationUpdate {
    private UUID userId;
    private UUID bookingId;
    private UUID tripId;
    private UUID typeId;
    private String channel;
    private String title;
    private String message;
    private Boolean isSent;
    private LocalDateTime sentAt;
}
