package com.bookvexe.notificationservice.dto.report;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class BookingsFilter {
    private String startDate; // yyyy-MM-dd
    private String endDate;   // yyyy-MM-dd
    private List<String> statuses; // CONFIRMED, PAID, CANCELLED, COMPLETED, PENDING
    private UUID customerTypeId;
}
