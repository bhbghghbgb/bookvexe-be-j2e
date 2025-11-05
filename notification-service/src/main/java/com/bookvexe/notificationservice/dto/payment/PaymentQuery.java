package com.bookvexe.notificationservice.dto.payment;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.models.dto.base.BasePageableQuery;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentQuery extends BasePageableQuery {
    private UUID bookingId;
    private UUID methodId;
    private String status;
    private String transactionCode;
}
