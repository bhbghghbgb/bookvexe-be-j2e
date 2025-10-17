
package org.example.bookvexebej2e.models.db;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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