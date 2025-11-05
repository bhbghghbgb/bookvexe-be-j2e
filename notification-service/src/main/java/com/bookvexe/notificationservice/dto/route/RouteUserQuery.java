package com.bookvexe.notificationservice.dto.route;

import lombok.Data;

@Data
public class RouteUserQuery {
    private String startLocation;
    private String endLocation;
    private String sortBy = "startLocation";
    private String sortDirection = "asc";
    private Integer page = 0;
    private Integer size = 10;
}
