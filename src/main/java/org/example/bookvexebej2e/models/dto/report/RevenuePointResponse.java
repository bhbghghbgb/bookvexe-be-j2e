package org.example.bookvexebej2e.models.dto.report;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevenuePointResponse {
    private String date;
    private BigDecimal totalRevenue;
    private Long transactionCount;
}
