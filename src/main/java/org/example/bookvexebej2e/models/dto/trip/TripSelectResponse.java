package org.example.bookvexebej2e.models.dto.trip;


import lombok.Data;
import org.example.bookvexebej2e.models.dto.route.RouteResponse;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TripSelectResponse {
    private UUID id;
    private RouteResponse route;
    private LocalDateTime departureTime;
}
