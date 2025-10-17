package org.example.bookvexebej2e.models.dto.user;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserResponse {
    private UUID id;
    private String username;
    private Boolean isGoogle;
    private String googleAccount;
    private UUID employeeId;
    private UUID customerId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
