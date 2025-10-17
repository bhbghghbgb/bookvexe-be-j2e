package org.example.bookvexebej2e.models.dto.route;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class RouteResponse {
    private UUID id;
    private String startLocation;
    private String endLocation;
    private BigDecimal distanceKm;
    private Integer estimatedDuration;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
