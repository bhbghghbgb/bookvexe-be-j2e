package com.bookvexe.notificationservice.dto.car;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.models.dto.base.BasePermissionResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class CarSeatResponse extends BasePermissionResponse {
    private UUID id;
    private String seatNumber;
    private String seatPosition;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> bookedStatuses; // Danh sách trạng thái đặt ghế (confirmed, pending, etc.)
    private Boolean isBooked; // Trạng thái ghế đã được đặt hay chưa
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
