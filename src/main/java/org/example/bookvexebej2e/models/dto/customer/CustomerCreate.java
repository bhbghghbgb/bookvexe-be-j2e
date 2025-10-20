package org.example.bookvexebej2e.models.dto.customer;

import lombok.Data;

import java.util.UUID;

@Data
public class CustomerCreate {
    private String code;
    private String name;
    private String email;
    private String phone;
    private UUID customerTypeId;
    private String description;
}
