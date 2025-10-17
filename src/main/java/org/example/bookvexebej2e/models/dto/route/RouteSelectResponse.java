package org.example.bookvexebej2e.models.dto.route;

import lombok.Data;

import java.util.UUID;

@Data
public class RouteSelectResponse {
    private UUID id;
    private String startLocation;
    private String endLocation;
}
