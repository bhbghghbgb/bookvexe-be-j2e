package com.bookvexe.notificationservice.entities;

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

    @Column(length = 50, unique = true, name = "invoiceNumber")
    private String invoiceNumber;

    @Column(length = 255, name = "fileUrl")
    private String fileUrl;

    @Column(name = "issuedAt")
    private LocalDateTime issuedAt;
}