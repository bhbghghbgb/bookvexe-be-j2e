package org.example.bookvexebej2e.models.dto.booking;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import lombok.Data;

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