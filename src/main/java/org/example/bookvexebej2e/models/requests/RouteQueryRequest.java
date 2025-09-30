package org.example.bookvexebej2e.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.bookvexebej2e.models.requests.base.BasePageableQueryRequest;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RouteQueryRequest extends BasePageableQueryRequest {
    private String startLocation;
    private String endLocation;
    private BigDecimal minDistance;
    private BigDecimal maxDistance;
    private Integer minDuration;
    private Integer maxDuration;
}
