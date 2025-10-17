package org.example.bookvexebej2e.dto.car;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CarEmployeeResponse {
    private UUID id;
    private UUID carId;
    private UUID employeeId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
