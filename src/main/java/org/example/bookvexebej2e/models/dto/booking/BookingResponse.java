package org.example.bookvexebej2e.models.dto.booking;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BookingResponse {
    private UUID id;
    private String code;
    private String type;
    private UUID userId;
    private UUID tripId;
    private UUID pickupStopId;
    private UUID dropoffStopId;
    private String bookingStatus;
    private BigDecimal totalPrice;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
