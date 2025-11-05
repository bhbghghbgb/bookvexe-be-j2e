package com.bookvexe.notificationservice.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevenueByMethodResponse {
    private String method;
    private BigDecimal total;
}
