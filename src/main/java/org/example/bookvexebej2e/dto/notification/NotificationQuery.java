package org.example.bookvexebej2e.dto.notification;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.dto.base.BasePageableQuery;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class NotificationQuery extends BasePageableQuery {
    private UUID userId;
    private UUID bookingId;
    private UUID tripId;
    private UUID typeId;
    private String channel;
    private Boolean isSent;
}
