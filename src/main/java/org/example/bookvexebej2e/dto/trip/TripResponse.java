package org.example.bookvexebej2e.dto.trip;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TripResponse {
    private UUID id;
    private UUID routeId;
    private LocalDateTime departureTime;
    private BigDecimal price;
    private Integer availableSeats;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
