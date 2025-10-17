package org.example.bookvexebej2e.dto.payment;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.dto.base.BasePageableQuery;

@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentMethodQuery extends BasePageableQuery {
    private String code;
    private String name;
    private Boolean isActive;
}
