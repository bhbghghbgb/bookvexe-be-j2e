package com.bookvexe.notificationservice.dto.car;

import lombok.Data;
import org.example.bookvexebej2e.models.dto.employee.EmployeeResponse;

import java.util.UUID;

@Data
public class CarEmployeeSelectResponse {
    private UUID id;
    private CarResponse car;
    private EmployeeResponse employee;
}
