package org.example.bookvexebej2e.models.db;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InvoiceDbModel extends BaseModel {
    @OneToOne
    @JoinColumn(name = "paymentId")
    private PaymentDbModel payment;

    @Column(length = 50, unique = true, name = "Số hóa đơn")
    private String invoiceNumber;

    @Column(length = 255, name = "File URL")
    private String fileUrl;

    @Column(name = "Ngày phát hành")
    private LocalDateTime issuedAt;
}