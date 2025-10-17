package org.example.bookvexebej2e.dto.customer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.dto.base.BasePageableQuery;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerQuery extends BasePageableQuery {
    private String code;
    private String name;
    private String email;
    private String phone;
    private UUID customerTypeId;
}
