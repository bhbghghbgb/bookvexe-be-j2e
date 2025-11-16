package org.example.bookvexebej2e.models.db;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InvoiceDbModel extends BaseModel {
    @Column(name = "payment_id")
    private UUID paymentId;

    @Column(length = 50, unique = true, name = "invoiceNumber")
    private String invoiceNumber;

    @Column(length = 255, name = "fileUrl")
    private String fileUrl;

    @Column(name = "issuedAt")
    private LocalDateTime issuedAt;
}