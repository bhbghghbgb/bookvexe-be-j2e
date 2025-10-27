package org.example.bookvexebej2e.models.dto.car;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class CarSelectResponse {
    private UUID id;
    private String code;
    private String licensePlate;
    private List<CarSeatSelectResponse> carSeats;
    private CarTypeSelectResponse carType;
}
