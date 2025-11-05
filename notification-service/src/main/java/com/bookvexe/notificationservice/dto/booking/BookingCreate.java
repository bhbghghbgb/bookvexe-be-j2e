package com.bookvexe.notificationservice.dto.booking;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class BookingCreate {
    private String code;
    private String type;
    private UUID customerId;
    private UUID tripId;
    private UUID pickupStopId;
    private UUID dropoffStopId;
    private String bookingStatus;
    private BigDecimal totalPrice;
    private List<BookingSeatCreate> bookingSeats;
}
