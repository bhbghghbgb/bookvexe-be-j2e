package com.bookvexe.notificationservice.dto.notification;

import lombok.Data;

import java.util.UUID;

@Data
public class NotificationTypeSelectResponse {
    private UUID id;
    private String code;
    private String name;
}
