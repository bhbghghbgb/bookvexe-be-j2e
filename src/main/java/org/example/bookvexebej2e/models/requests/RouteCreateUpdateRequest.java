package org.example.bookvexebej2e.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RouteCreateUpdateRequest {
    private String startLocation;
    private String endLocation;
    private BigDecimal distanceKm;
    private Integer estimatedDuration; // minutes
}
