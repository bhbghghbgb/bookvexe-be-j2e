package com.bookvexe.notificationservice.dto.car;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.models.dto.base.BasePageableQuery;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class CarQuery extends BasePageableQuery {
    private UUID carTypeId;
    private String code;
    private String licensePlate;
    private Boolean isDeleted;
}
