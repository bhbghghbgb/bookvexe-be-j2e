package org.example.bookvexebej2e.models.dto.car;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.models.dto.base.BasePermissionResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CarResponse extends BasePermissionResponse {
    private UUID id;
    private String code;
    private CarTypeResponse carType;
    private String licensePlate;
    private List<CarSeatResponse> carSeats;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean isDeleted;

    public CarResponse() {
        super();
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
        setPermissionsByDeletedStatus(isDeleted);
    }
}
