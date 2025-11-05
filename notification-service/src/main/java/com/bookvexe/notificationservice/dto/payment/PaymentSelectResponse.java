package com.bookvexe.notificationservice.dto.payment;

import lombok.Data;

import java.util.UUID;

@Data
public class PaymentSelectResponse {
    private UUID id;
    private String transactionCode;
    private String status;
}
