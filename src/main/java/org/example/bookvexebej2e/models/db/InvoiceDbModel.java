package org.example.bookvexebej2e.models.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.bookvexebej2e.models.db.embeds.CreateAudit;
import org.example.bookvexebej2e.models.db.embeds.IssueAudit;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class InvoiceDbModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Integer invoiceId;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private PaymentDbModel payment;

    @Column(name = "invoice_number", unique = true, nullable = false, length = 50)
    private String invoiceNumber;

    @Column(name = "file_url", length = 255)
    private String fileUrl;

    @Embedded
    private IssueAudit issueAudit = new IssueAudit();

    @Embedded
    private CreateAudit createAudit = new CreateAudit();

    public LocalDateTime getIssuedAt() {
        return issueAudit != null ? issueAudit.getIssuedAt() : null;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        if (issueAudit == null)
            issueAudit = new IssueAudit();
        issueAudit.setIssuedAt(issuedAt);
    }

    public LocalDateTime getCreatedAt() {
        return createAudit != null ? createAudit.getCreatedAt() : null;
    }
}