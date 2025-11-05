package com.bookvexe.notificationservice.dto.invoice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.models.dto.base.BasePermissionResponse;
import org.example.bookvexebej2e.models.dto.payment.PaymentResponse;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class InvoiceResponse extends BasePermissionResponse {
    private UUID id;
    @JsonIgnoreProperties("invoice")
    private PaymentResponse payment;
    private String invoiceNumber;
    private String fileUrl;
    private LocalDateTime issuedAt;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean isDeleted;

    public InvoiceResponse() {
        super();
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
        setPermissionsByDeletedStatus(isDeleted);
    }
}
