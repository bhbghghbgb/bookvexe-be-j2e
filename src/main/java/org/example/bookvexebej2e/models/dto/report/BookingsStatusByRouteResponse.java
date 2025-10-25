package org.example.bookvexebej2e.models.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingsStatusByRouteResponse {
    private String routeName;
    private Long cancelled;
    private Long success;
}
