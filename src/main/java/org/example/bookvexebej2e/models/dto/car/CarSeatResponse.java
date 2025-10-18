package org.example.bookvexebej2e.models.dto.car;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CarSeatResponse {
    private UUID id;
    //    private CarResponse car;
    private String seatNumber;
    private String seatPosition;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean isDeleted;
}
