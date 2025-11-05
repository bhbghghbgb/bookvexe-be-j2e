package com.bookvexe.notificationservice.dto.report;

import lombok.Data;

import java.util.UUID;

@Data
public class TripsFilter {
    private String startDate; // yyyy-MM-dd
    private String endDate;   // yyyy-MM-dd
    private UUID routeId;
    private UUID carTypeId;
}
