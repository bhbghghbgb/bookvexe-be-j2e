package com.bookvexe.notificationservice.dto.customer;

import lombok.Data;

import java.util.UUID;

@Data
public class CustomerTypeSelectResponse {
    private UUID id;
    private String code;
    private String name;
}
