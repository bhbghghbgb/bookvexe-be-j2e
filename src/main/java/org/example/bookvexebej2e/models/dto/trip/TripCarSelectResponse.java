package org.example.bookvexebej2e.models.dto.trip;

import lombok.Data;

import java.util.UUID;

@Data
public class TripCarSelectResponse {
    private UUID id;
    private UUID tripId;
    private UUID carId;
}
