package org.example.bookvexebej2e.models.dto.notification;

import lombok.Data;
import org.example.bookvexebej2e.models.dto.booking.BookingResponse;
import org.example.bookvexebej2e.models.dto.trip.TripResponse;
import org.example.bookvexebej2e.models.dto.user.UserResponse;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class NotificationResponse {
    private UUID id;
    private UserResponse user;
    private BookingResponse booking;
    private TripResponse trip;
    private NotificationTypeResponse type;
    private String channel;
    private String title;
    private String message;
    private Boolean isSent;
    private LocalDateTime sentAt;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean isDeleted;
}
