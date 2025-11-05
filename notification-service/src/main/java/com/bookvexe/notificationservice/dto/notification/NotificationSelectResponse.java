package com.bookvexe.notificationservice.dto.notification;

import lombok.Data;

import java.util.UUID;

@Data
public class NotificationSelectResponse {
    private UUID id;
    private String title;
    private String channel;
}
