package org.example.bookvexebej2e.models.dto.car;

import lombok.Data;

import java.util.UUID;

@Data
public class CarEmployeeCreate {
    private UUID carId;
    private UUID employeeId;
}
