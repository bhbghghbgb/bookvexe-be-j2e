package org.example.bookvexebej2e.models.dto.booking;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.example.bookvexebej2e.models.dto.car.CarSeatResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.models.dto.base.BasePermissionResponse;

@Data
@EqualsAndHashCode(callSuper = true)
public class BookingSeatResponse extends BasePermissionResponse {
    private UUID id;
    private CarSeatResponse seat;
    private String status;
    private BigDecimal price;
    private String code;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean isDeleted;

    public BookingSeatResponse() {
        super();
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
        setPermissionsByDeletedStatus(isDeleted);
    }
}
