package org.example.bookvexebej2e.dto.booking;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class BookingSeatUpdate {
    private UUID bookingId;
    private UUID seatId;
    private String status;
    private BigDecimal price;
}
