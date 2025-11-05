package com.bookvexe.notificationservice.dto.route;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

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
