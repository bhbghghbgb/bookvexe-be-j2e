package com.bookvexe.notificationservice.dto.trip;

import lombok.Data;
import org.example.bookvexebej2e.models.dto.route.RouteResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class TripSelectResponse {
    private UUID id;
    private RouteResponse route;
    private LocalDateTime departureTime;
    private BigDecimal price;
    private List<TripCarSelectResponse> tripCars;
    private List<TripStopSelectResponse> tripStops;
}
