package org.example.bookvexebej2e.dto.customer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.dto.base.BasePageableQuery;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerTypeQuery extends BasePageableQuery {
    private String code;
    private String name;
}
