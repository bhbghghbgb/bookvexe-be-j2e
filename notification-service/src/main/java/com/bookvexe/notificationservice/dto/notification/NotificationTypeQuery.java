package com.bookvexe.notificationservice.dto.notification;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.models.dto.base.BasePageableQuery;

@Data
@EqualsAndHashCode(callSuper = true)
public class NotificationTypeQuery extends BasePageableQuery {
    private String code;
    private String name;
}
