package com.bookvexe.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationKafkaDTO {
    private Long userId;
    private String message;
    private String type;
    private String link;
}

