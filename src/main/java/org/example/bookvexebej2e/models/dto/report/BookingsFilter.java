package org.example.bookvexebej2e.models.dto.report;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class BookingsFilter {
    private String startDate; // yyyy-MM-dd
    private String endDate;   // yyyy-MM-dd
    private List<String> statuses; // CONFIRMED, PAID, CANCELLED, COMPLETED, PENDING
    private UUID customerTypeId;
    private String printedBy;
}
