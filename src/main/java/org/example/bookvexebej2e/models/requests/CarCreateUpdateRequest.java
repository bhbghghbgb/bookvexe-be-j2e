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
    private String carTypeId;
    private String licensePlate;
}
