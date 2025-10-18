package org.example.bookvexebej2e.models.dto.car;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class CarResponse {
    private UUID id;
    private CarTypeResponse carType;
    private String licensePlate;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean isDeleted;
}
