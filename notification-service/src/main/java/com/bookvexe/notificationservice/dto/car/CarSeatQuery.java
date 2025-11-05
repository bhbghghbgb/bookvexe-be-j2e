package com.bookvexe.notificationservice.dto.car;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.models.dto.base.BasePageableQuery;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class CarSeatQuery extends BasePageableQuery {
    private UUID carId;
    private String seatNumber;
    private String seatPosition;
}
