package org.example.bookvexebej2e.dto.customer;

import lombok.Data;

import java.util.UUID;

@Data
public class CustomerUpdate {
    private String code;
    private String name;
    private String email;
    private String phone;
    private UUID customerTypeId;
    private String description;
}
