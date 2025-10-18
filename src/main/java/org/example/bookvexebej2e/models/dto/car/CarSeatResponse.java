package org.example.bookvexebej2e.models.dto.car;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class CarSeatResponse {
    private UUID id;
    private CarResponse car;
    private String seatNumber;
    private String seatPosition;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean isDeleted;
}
