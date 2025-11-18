package org.example.bookvexebej2e.models.dto.report;

import java.util.UUID;

import lombok.Data;

@Data
public class RevenueFilter {
    private String startDate; // yyyy-MM-dd
    private String endDate;   // yyyy-MM-dd
    private UUID routeId;
    private UUID methodId;
    private String status; // PENDING, SUCCESS, FAILED, REFUNDED
    private String printedBy;
}
