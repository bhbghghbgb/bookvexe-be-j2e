package org.example.bookvexebej2e.models.dto.report;

import java.util.UUID;

import lombok.Data;

@Data
public class TripsFilter {
    private String startDate; // yyyy-MM-dd
    private String endDate;   // yyyy-MM-dd
    private UUID routeId;
    private UUID carTypeId;
    private String printedBy;
}
