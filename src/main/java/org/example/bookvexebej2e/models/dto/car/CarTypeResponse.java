package org.example.bookvexebej2e.models.dto.car;

import java.time.LocalDateTime;
import java.util.UUID;

import org.example.bookvexebej2e.models.dto.base.BasePermissionResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CarTypeResponse extends BasePermissionResponse {
    private UUID id;
    private String code;
    private String name;
    private String description;
    private Integer seatCount;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean isDeleted;

    public CarTypeResponse() {
        super();
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
        setPermissionsByDeletedStatus(isDeleted);
    }
}
