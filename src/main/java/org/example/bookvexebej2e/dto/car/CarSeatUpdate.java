package org.example.bookvexebej2e.dto.car;

import lombok.Data;
import java.util.UUID;

@Data
public class CarSeatUpdate {
    private UUID carId;
    private String seatNumber;
    private String seatPosition;
}
