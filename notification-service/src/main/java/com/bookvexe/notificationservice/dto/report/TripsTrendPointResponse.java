package com.bookvexe.notificationservice.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripsTrendPointResponse {
    private String date;
    private Long cancelledTrips;
    private Long completedTrips;
}
