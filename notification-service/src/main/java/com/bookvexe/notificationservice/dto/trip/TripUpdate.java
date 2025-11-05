package com.bookvexe.notificationservice.dto.trip;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripUpdate {
    private UUID routeId;

    private List<UUID> carIds;

    private LocalDateTime departureTime;

    private BigDecimal price;

    private Integer availableSeats;
}