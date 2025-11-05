package com.bookvexe.notificationservice.dto.customer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.models.dto.base.BasePageableQuery;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerTypeQuery extends BasePageableQuery {
    private String code;
    private String name;
    private Boolean isDeleted;
}
