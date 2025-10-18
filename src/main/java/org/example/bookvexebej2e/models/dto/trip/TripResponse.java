package org.example.bookvexebej2e.models.dto.trip;

import lombok.Data;
import org.example.bookvexebej2e.models.dto.route.RouteResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TripResponse {
    private UUID id;
    private RouteResponse route;
    private LocalDateTime departureTime;
    private BigDecimal price;
    private Integer availableSeats;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean isDeleted;
}
