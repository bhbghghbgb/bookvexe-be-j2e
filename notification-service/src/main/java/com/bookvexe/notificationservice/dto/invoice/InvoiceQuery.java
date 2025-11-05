package com.bookvexe.notificationservice.dto.invoice;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.models.dto.base.BasePageableQuery;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class InvoiceQuery extends BasePageableQuery {
    private UUID paymentId;
    private String invoiceNumber;
}
