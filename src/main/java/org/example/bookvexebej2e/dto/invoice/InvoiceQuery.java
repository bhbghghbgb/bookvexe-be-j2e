package org.example.bookvexebej2e.dto.invoice;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.dto.base.BasePageableQuery;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class InvoiceQuery extends BasePageableQuery {
    private UUID paymentId;
    private String invoiceNumber;
}
