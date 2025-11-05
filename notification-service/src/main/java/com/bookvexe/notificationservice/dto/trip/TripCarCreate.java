package com.bookvexe.notificationservice.dto.trip;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TripCarCreate {
    private UUID tripId;
    private UUID carId;
    private BigDecimal price;
    private Integer availableSeats;
}
