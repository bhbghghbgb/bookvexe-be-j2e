package org.example.bookvexebej2e.dto.route;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.dto.base.BasePageableQuery;

@Data
@EqualsAndHashCode(callSuper = true)
public class RouteQuery extends BasePageableQuery {
    private String startLocation;
    private String endLocation;
}
