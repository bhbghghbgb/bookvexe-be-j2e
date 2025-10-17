package org.example.bookvexebej2e.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarCreateUpdateRequest {
    private Integer ownerId; // optional
    private Integer carTypeId;
    private String licensePlate;
    private Integer seatCount;
}
