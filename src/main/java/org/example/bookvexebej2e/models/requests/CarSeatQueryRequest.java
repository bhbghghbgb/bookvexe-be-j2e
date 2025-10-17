package org.example.bookvexebej2e.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.bookvexebej2e.models.requests.base.BasePageableQueryRequest;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarSeatQueryRequest extends BasePageableQueryRequest {
    private String carId;
    private String seatNumber;
}
