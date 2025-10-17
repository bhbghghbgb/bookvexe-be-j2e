package org.example.bookvexebej2e.models.dto.user;

import lombok.Data;
import org.example.bookvexebej2e.models.dto.customer.CustomerResponse;
import org.example.bookvexebej2e.models.dto.employee.EmployeeResponse;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserResponse {
    private UUID id;
    private String username;
    private Boolean isGoogle;
    private String googleAccount;
    private EmployeeResponse employee;
    private CustomerResponse customer;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
