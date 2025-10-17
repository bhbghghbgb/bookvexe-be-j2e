package org.example.bookvexebej2e.dto.car;

import lombok.Data;

import java.util.UUID;

@Data
public class CarEmployeeUpdate {
    private UUID carId;
    private UUID employeeId;
}
