package org.example.bookvexebej2e.models.dto.trip;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.example.bookvexebej2e.models.dto.car.CarResponse;

import lombok.Data;

@Data
public class TripCarResponse {
    private UUID id;
    private TripResponse trip;
    private CarResponse car;
    private BigDecimal price;
    private Integer availableSeats;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean isDeleted;
}
