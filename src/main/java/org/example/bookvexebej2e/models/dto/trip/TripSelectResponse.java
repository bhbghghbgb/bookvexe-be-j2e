package org.example.bookvexebej2e.models.dto.trip;

import java.time.LocalDateTime;
import java.util.UUID;

import org.example.bookvexebej2e.models.dto.route.RouteResponse;

import lombok.Data;

@Data
public class TripSelectResponse {
    private UUID id;
    private RouteResponse route;
    private LocalDateTime departureTime;
}
