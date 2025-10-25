package org.example.bookvexebej2e.models.dto.trip;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.models.dto.base.BasePermissionResponse;
import org.example.bookvexebej2e.models.dto.route.RouteResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TripUserResponse extends BasePermissionResponse {
    private UUID id;
    private RouteResponse route;
    private LocalDateTime departureTime;
    private BigDecimal price;
    private Integer availableSeats;
    private List<TripStopResponse> tripStops;
    private List<TripCarResponse> tripCars;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean isDeleted;
}