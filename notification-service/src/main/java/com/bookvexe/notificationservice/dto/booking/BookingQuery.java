package com.bookvexe.notificationservice.dto.booking;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.models.dto.base.BasePageableQuery;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class BookingQuery extends BasePageableQuery {
    private String code;
    private String type;
    private String bookingStatus;
    private UUID customerId;
    private UUID tripId;
}
