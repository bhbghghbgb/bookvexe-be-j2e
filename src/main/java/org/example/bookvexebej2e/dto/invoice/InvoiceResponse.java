package org.example.bookvexebej2e.dto.invoice;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class InvoiceResponse {
    private UUID id;
    private UUID paymentId;
    private String invoiceNumber;
    private String fileUrl;
    private LocalDateTime issuedAt;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
