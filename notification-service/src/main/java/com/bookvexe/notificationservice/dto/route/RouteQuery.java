package com.bookvexe.notificationservice.dto.route;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.models.dto.base.BasePageableQuery;

@Data
@EqualsAndHashCode(callSuper = true)
public class RouteQuery extends BasePageableQuery {
    private String startLocation;
    private String endLocation;
    private Double minDistanceKm;
    private Double maxDistanceKm;
    private Integer minEstimatedDuration;
    private Integer maxEstimatedDuration;
    private Boolean isDeleted;
}