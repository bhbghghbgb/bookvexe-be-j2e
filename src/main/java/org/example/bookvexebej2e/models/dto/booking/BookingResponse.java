package org.example.bookvexebej2e.models.dto.booking;

import lombok.Data;
import org.example.bookvexebej2e.models.dto.trip.TripResponse;
import org.example.bookvexebej2e.models.dto.trip.TripStopResponse;
import org.example.bookvexebej2e.models.dto.user.UserResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BookingResponse {
    private UUID id;
    private String code;
    private String type;
    private UserResponse user;
    private TripResponse trip;
    private TripStopResponse pickupStop;
    private TripStopResponse dropoffStop;
    private String bookingStatus;
    private BigDecimal totalPrice;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean isDeleted;
}
