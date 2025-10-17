package org.example.bookvexebej2e.models.dto.invoice;

import lombok.Data;
import org.example.bookvexebej2e.models.dto.payment.PaymentResponse;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class InvoiceResponse {
    private UUID id;
    private PaymentResponse payment;
    private String invoiceNumber;
    private String fileUrl;
    private LocalDateTime issuedAt;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
