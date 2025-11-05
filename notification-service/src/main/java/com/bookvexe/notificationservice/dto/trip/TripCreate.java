package com.bookvexe.notificationservice.dto.trip;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class TripCreate {
    private UUID routeId;
    private LocalDateTime departureTime;
    private BigDecimal price;
    private Integer availableSeats;
    private List<UUID> carIds; // Danh sách ID các xe được chọn cho trip
}
