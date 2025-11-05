package com.bookvexe.notificationservice.dto.booking;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class BookingUserCreate {
    private String customerName;
    private String customerEmail;
    private String customerPhone;

    private String type;
    private UUID tripId;
    private UUID pickupStopId;
    private UUID dropoffStopId;
    private BigDecimal totalPrice;
    private List<BookingSeatCreate> bookingSeats;
}