package org.example.bookvexebej2e.dto.booking;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class BookingUpdate {
    private String code;
    private String type;
    private UUID userId;
    private UUID tripId;
    private UUID pickupStopId;
    private UUID dropoffStopId;
    private String bookingStatus;
    private BigDecimal totalPrice;
}
