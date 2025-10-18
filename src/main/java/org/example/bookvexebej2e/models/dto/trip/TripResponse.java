package org.example.bookvexebej2e.models.dto.trip;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.models.dto.route.RouteResponse;

import lombok.Data;

@Data
public class TripResponse {
    private UUID id;
    private RouteResponse route;
    private LocalDateTime departureTime;
    private List<TripCarResponse> tripCars;
    private List<TripStopResponse> tripStops;
    private BigDecimal price;
    private Integer availableSeats;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean isDeleted;
}
