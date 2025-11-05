package com.bookvexe.notificationservice.dto.invoice;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class InvoiceCreate {
    private UUID paymentId;
    private String invoiceNumber;
    private String fileUrl;
    private LocalDateTime issuedAt;
}
