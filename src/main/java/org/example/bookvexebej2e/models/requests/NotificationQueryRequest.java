package org.example.bookvexebej2e.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.bookvexebej2e.models.requests.base.BasePageableQueryRequest;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationQueryRequest extends BasePageableQueryRequest {
    private Integer userId;
    private Integer bookingId;
    private Integer tripId;
    private Integer typeId;
    private Boolean isSent;
    private String channel;
    private LocalDateTime createdAfter;
    private LocalDateTime createdBefore;
    private LocalDateTime sentAfter;
    private LocalDateTime sentBefore;
}
