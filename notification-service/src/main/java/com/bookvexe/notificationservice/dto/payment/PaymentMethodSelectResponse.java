package com.bookvexe.notificationservice.dto.payment;

import lombok.Data;

import java.util.UUID;

@Data
public class PaymentMethodSelectResponse {
    private UUID id;
    private String code;
    private String name;
}
