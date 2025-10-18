package org.example.bookvexebej2e.models.dto.trip;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.models.dto.base.BasePermissionResponse;

@Data
@EqualsAndHashCode(callSuper = true)
public class TripStopResponse extends BasePermissionResponse {
    private UUID id;
    // private TripResponse trip;
    private String stopType;
    private String location;
    private Integer orderIndex;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean isDeleted;

    public TripStopResponse() {
        super();
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
        setPermissionsByDeletedStatus(isDeleted);
    }
}
