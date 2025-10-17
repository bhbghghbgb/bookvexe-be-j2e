package org.example.bookvexebej2e.dto.route;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class RouteUpdate {
    private String startLocation;
    private String endLocation;
    private BigDecimal distanceKm;
    private Integer estimatedDuration;
}
