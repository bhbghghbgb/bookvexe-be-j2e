package org.example.bookvexebej2e.models.dto.customer;

import java.util.UUID;

import lombok.Data;

@Data
public class CustomerSelectResponse {
    private UUID id;
    private String code;
    private String name;
    private String email;
    private UUID userId;
}
