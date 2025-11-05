package com.bookvexe.notificationservice.dto.notification;

import lombok.Data;

@Data
public class NotificationTypeCreate {
    private String code;
    private String name;
    private String description;
}
