package com.bookvexe.notificationservice.dto.booking;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class BookingSeatCreate {
    private UUID bookingId;
    private UUID seatId;
    private String status;
    private BigDecimal price;
}
