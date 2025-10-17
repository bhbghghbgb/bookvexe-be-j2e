package org.example.bookvexebej2e.models.dto.car;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CarTypeResponse {
    private UUID id;
    private String code;
    private String name;
    private String description;
    private Integer seatCount;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean isDeleted;
}
