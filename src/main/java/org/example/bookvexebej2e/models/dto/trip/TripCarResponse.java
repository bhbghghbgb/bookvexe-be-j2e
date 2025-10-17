package org.example.bookvexebej2e.models.dto.trip;

import lombok.Data;
import org.example.bookvexebej2e.models.dto.car.CarResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

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
