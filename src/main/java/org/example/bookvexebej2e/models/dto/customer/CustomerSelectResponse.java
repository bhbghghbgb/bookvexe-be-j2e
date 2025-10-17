package org.example.bookvexebej2e.models.dto.customer;

import lombok.Data;

import java.util.UUID;

@Data
public class CustomerSelectResponse {
    private UUID id;
    private String code;
    private String name;
    private String email;
}
