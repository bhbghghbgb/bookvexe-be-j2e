package org.example.bookvexebej2e.models.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.bookvexebej2e.models.db.embeds.CreateAudit;
import org.example.bookvexebej2e.models.db.embeds.UpdateAudit;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PaymentDbModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Integer paymentId;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private BookingDbModel booking;

    @ManyToOne
    @JoinColumn(name = "method_id")
    private PaymentMethodDbModel method;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "pending";

    @Column(name = "transaction_code", length = 100)
    private String transactionCode;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Embedded
    private CreateAudit createAudit = new CreateAudit();

    @Embedded
    private UpdateAudit updateAudit = new UpdateAudit();

    public LocalDateTime getCreatedAt() {
        return createAudit != null ? createAudit.getCreatedAt() : null;
    }

    public LocalDateTime getUpdatedAt() {
        return updateAudit != null ? updateAudit.getUpdatedAt() : null;
    }
}
