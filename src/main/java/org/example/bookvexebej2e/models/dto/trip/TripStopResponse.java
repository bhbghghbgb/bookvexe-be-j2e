package org.example.bookvexebej2e.models.dto.trip;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TripStopResponse {
    private UUID id;
    //    private TripResponse trip;
    private String stopType;
    private String location;
    private Integer orderIndex;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean isDeleted;
}
