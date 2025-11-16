package org.example.bookvexebej2e.models.dto.report;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingsByRouteResponse {
    private String routeName;
    private Long totalBookings;
    private BigDecimal routeRevenue;
}
