package com.bookvexe.notificationservice.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripsStatusResponse {
    private String status; // "Đã chạy" | "Bị hủy"
    private Long total;
}
