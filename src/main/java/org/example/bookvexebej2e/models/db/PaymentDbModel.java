
package org.example.bookvexebej2e.models.db;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

    @Column(precision = 10, scale = 2, name = "Số tiền")
    private BigDecimal amount;

    @Column(length = 20, name = "Trạng thái")
    private String status;

    @Column(length = 100, name = "Mã giao dịch")
    private String transactionCode;

    @Column(name = "Ngày thanh toán")
    private LocalDateTime paidAt;

    @OneToOne(mappedBy = "payment")
    private InvoiceDbModel invoice;
}
