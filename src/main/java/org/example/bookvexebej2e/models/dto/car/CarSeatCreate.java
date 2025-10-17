package org.example.bookvexebej2e.models.dto.car;

import lombok.Data;

import java.util.UUID;

@Data
public class CarSeatCreate {
    private UUID carId;
    private String seatNumber;
    private String seatPosition;
}
