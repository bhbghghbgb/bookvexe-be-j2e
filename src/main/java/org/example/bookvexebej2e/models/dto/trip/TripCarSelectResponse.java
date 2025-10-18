package org.example.bookvexebej2e.models.dto.trip;

import java.math.BigDecimal;
import java.util.UUID;

import org.example.bookvexebej2e.models.dto.car.CarSelectResponse;

import lombok.Data;

@Data
public class TripCarSelectResponse {
    private UUID id;
    private CarSelectResponse car;
    private BigDecimal price;
}
