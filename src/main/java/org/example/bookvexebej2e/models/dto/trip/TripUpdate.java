package org.example.bookvexebej2e.models.dto.trip;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class TripUpdate {
    private UUID routeId;
    private LocalDateTime departureTime;
    private BigDecimal price;
    private Integer availableSeats;
    private List<UUID> carIds; // Danh sách ID các xe được chọn cho trip
}
