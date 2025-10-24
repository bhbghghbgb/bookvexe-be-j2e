package org.example.bookvexebej2e.models.dto.route;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteUserResponse {
    private UUID id;
    private String location;

    public RouteUserResponse(String location) {
        this.location = location;
    }
}
