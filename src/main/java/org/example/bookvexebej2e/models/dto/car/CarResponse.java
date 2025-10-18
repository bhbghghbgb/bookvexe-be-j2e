package org.example.bookvexebej2e.models.dto.car;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class CarResponse {
    private UUID id;
    private CarTypeResponse carType;
    private String licensePlate;
    private List<CarSeatResponse> carSeats;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean isDeleted;
}
