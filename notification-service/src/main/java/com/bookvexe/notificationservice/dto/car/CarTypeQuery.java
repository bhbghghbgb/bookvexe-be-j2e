package com.bookvexe.notificationservice.dto.car;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.models.dto.base.BasePageableQuery;

@Data
@EqualsAndHashCode(callSuper = true)
public class CarTypeQuery extends BasePageableQuery {
    private String code;
    private String name;
    private Boolean isDeleted;
}
