package com.bookvexe.notificationservice.dto.trip;

import lombok.Data;
import org.example.bookvexebej2e.models.dto.car.CarSelectResponse;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TripCarSelectResponse {
    private UUID id;
    private CarSelectResponse car;
    private BigDecimal price;
    private Integer availableSeats;
}
