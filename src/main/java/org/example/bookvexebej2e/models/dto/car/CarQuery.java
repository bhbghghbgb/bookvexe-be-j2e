package org.example.bookvexebej2e.models.dto.car;

import java.util.UUID;

import org.example.bookvexebej2e.models.dto.base.BasePageableQuery;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CarQuery extends BasePageableQuery {
    private UUID carTypeId;
    private String code;
    private String licensePlate;
    private Boolean isDeleted;
}
