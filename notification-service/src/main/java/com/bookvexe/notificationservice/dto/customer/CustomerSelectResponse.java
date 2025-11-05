package com.bookvexe.notificationservice.dto.customer;

import lombok.Data;

import java.util.UUID;

@Data
public class CustomerSelectResponse {
    private UUID id;
    private String code;
    private String name;
    private String email;
    private UUID userId;
}
