package com.bookvexe.notificationservice.dto.report;

import lombok.Data;

import java.util.UUID;

@Data
public class RevenueFilter {
    private String startDate; // yyyy-MM-dd
    private String endDate;   // yyyy-MM-dd
    private UUID routeId;
    private UUID methodId;
    private String status; // PENDING, SUCCESS, FAILED, REFUNDED
}
