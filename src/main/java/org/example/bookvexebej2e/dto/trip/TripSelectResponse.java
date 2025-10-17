package org.example.bookvexebej2e.dto.trip;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TripSelectResponse {
    private UUID id;
    private UUID routeId;
    private LocalDateTime departureTime;
}
