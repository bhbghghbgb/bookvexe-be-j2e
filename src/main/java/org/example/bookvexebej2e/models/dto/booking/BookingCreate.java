package org.example.bookvexebej2e.models.dto.booking;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import lombok.Data;

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
