package org.example.bookvexebej2e.models.dto.seat;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatUpdatePayload {
    private String tripId;
    private String carId;
    private List<String> seatIds;
    private String action; // "hold" or "release"
    private String holdUntil; // ISO timestamp (optional)
    private String by; // user id or name (optional)
}