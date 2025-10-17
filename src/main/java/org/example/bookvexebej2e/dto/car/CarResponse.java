package org.example.bookvexebej2e.dto.car;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CarResponse {
    private UUID id;
    private UUID carTypeId;
    private String licensePlate;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
