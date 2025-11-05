package com.bookvexe.notificationservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentDbModel extends BaseModel {
    @OneToOne
    @JoinColumn(name = "bookingId")
    private BookingDbModel booking;

    @ManyToOne
    @JoinColumn(name = "methodId")
    private PaymentMethodDbModel method;

    @Column(precision = 10, scale = 2, name = "amount")
    private BigDecimal amount;

    @Column(length = 20, name = "status")
    private String status;

    @Column(length = 100, name = "transactionCode")
    private String transactionCode;

    @Column(name = "paidAt")
    private LocalDateTime paidAt;

    @OneToOne(mappedBy = "payment")
    private InvoiceDbModel invoice;
}
