package org.example.bookvexebej2e.models.dto.trip;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TripCarResponse {
    private UUID id;
    private UUID tripId;
    private UUID carId;
    private BigDecimal price;
    private Integer availableSeats;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
