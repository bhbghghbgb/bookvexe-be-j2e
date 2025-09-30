package org.example.bookvexebej2e.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.bookvexebej2e.models.requests.base.BasePageableQueryRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentQueryRequest extends BasePageableQueryRequest {
    private Integer bookingId;
    private Integer methodId;
    private String status;
    private List<String> statuses;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private LocalDateTime createdAfter;
    private LocalDateTime createdBefore;
    private LocalDateTime paidAfter;
    private LocalDateTime paidBefore;
}
