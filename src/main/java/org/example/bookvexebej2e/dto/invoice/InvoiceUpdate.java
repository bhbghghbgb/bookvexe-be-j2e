package org.example.bookvexebej2e.dto.invoice;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class InvoiceUpdate {
    private UUID paymentId;
    private String invoiceNumber;
    private String fileUrl;
    private LocalDateTime issuedAt;
}
