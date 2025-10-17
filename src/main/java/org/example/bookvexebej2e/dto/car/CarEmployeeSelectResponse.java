package org.example.bookvexebej2e.dto.car;

import lombok.Data;
import java.util.UUID;

@Data
public class CarEmployeeSelectResponse {
    private UUID id;
    private UUID carId;
    private UUID employeeId;
}
