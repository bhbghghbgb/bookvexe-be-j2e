package org.example.bookvexebej2e.dto.car;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.dto.base.BasePageableQuery;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class CarEmployeeQuery extends BasePageableQuery {
    private UUID carId;
    private UUID employeeId;
}
