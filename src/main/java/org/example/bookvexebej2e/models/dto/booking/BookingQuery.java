package org.example.bookvexebej2e.models.dto.booking;

import java.util.UUID;

import org.example.bookvexebej2e.models.dto.base.BasePageableQuery;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BookingQuery extends BasePageableQuery {
    private String code;
    private String type;
    private String bookingStatus;
    private UUID customerId;
    private UUID tripId;
}
