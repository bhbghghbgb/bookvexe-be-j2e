package org.example.bookvexebej2e.models.dto.trip;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TripCreate {
    private UUID routeId;
    private LocalDateTime departureTime;
    private BigDecimal price;
    private Integer availableSeats;
}
