package com.bookvexe.notificationservice.dto.invoice;

import lombok.Data;

import java.util.UUID;

@Data
public class InvoiceSelectResponse {
    private UUID id;
    private String invoiceNumber;
}
