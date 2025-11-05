package com.bookvexe.notificationservice.dto.booking;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.models.dto.base.BasePageableQuery;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class BookingSeatQuery extends BasePageableQuery {
    private UUID bookingId;
    private UUID seatId;
    private String status;
}
