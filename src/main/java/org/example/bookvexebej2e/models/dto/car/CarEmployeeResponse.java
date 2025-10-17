package org.example.bookvexebej2e.models.dto.car;

import lombok.Data;
import org.example.bookvexebej2e.models.dto.employee.EmployeeResponse;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CarEmployeeResponse {
    private UUID id;
    private CarResponse car;
    private EmployeeResponse employee;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
