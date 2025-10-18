package org.example.bookvexebej2e.models.dto.car;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class CarResponse {
    private UUID id;
    private String code;
    private CarTypeResponse carType;
    private String licensePlate;
    private List<CarSeatResponse> carSeats;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean isDeleted;
}
