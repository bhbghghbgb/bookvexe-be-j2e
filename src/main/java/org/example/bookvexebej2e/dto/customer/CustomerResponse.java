package org.example.bookvexebej2e.dto.customer;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CustomerResponse {
    private UUID id;
    private String code;
    private String name;
    private String email;
    private String phone;
    private UUID customerTypeId;
    private String description;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
