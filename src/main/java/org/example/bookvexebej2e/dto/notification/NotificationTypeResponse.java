package org.example.bookvexebej2e.dto.notification;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class NotificationTypeResponse {
    private UUID id;
    private String code;
    private String name;
    private String description;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
