package org.example.bookvexebej2e.helpers.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PaymentDto {
    private UUID id;
    private String status;
}
