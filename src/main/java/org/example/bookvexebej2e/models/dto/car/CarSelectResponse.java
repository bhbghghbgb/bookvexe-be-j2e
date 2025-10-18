package org.example.bookvexebej2e.models.dto.car;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CarSelectResponse {
    private UUID id;
    private String licensePlate;
    private List<CarSeatSelectResponse> carSeats;
}
