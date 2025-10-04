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
public class CarQueryRequest extends BasePageableQueryRequest {
    private String licensePlate;
    private Integer ownerId;
    private Integer carTypeId;
    private Integer minSeatCount;
    private Integer maxSeatCount;
    private Boolean active;
}
