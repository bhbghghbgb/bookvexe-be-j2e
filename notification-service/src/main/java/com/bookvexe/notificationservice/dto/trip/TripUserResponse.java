package com.bookvexe.notificationservice.dto.trip;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.models.dto.base.BasePermissionResponse;
import org.example.bookvexebej2e.models.dto.route.RouteResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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