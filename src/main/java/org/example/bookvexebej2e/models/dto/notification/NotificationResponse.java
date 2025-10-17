package org.example.bookvexebej2e.models.dto.notification;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class NotificationResponse {
    private UUID id;
    private UUID userId;
    private UUID bookingId;
    private UUID tripId;
    private UUID typeId;
    private String channel;
    private String title;
    private String message;
    private Boolean isSent;
    private LocalDateTime sentAt;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
