package org.example.bookvexebej2e.models.dto.car;

import java.time.LocalDateTime;
import java.util.UUID;

import org.example.bookvexebej2e.models.dto.base.BasePermissionResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CarSeatResponse extends BasePermissionResponse {
    private UUID id;
    private String seatNumber;
    private String seatPosition;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean isDeleted;

    public CarSeatResponse() {
        super();
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
        setPermissionsByDeletedStatus(isDeleted);
    }
}
