package org.example.bookvexebej2e.dto.car;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.dto.base.BasePageableQuery;

@Data
@EqualsAndHashCode(callSuper = true)
public class CarTypeQuery extends BasePageableQuery {
    private String code;
    private String name;
}
