package org.example.bookvexebej2e.models.dto.trip;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.example.bookvexebej2e.models.dto.base.BasePermissionResponse;
import org.example.bookvexebej2e.models.dto.car.CarResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TripCarResponse extends BasePermissionResponse {
    private UUID id;
    private UUID tripId;
    private UUID carId;
    private CarResponse car;
    private BigDecimal price;
    private Integer availableSeats;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean isDeleted;

    public TripCarResponse() {
        super();
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
        setPermissionsByDeletedStatus(isDeleted);
    }
}
