package org.example.bookvexebej2e.models.dto.employee;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.models.dto.base.BasePageableQuery;

@Data
@EqualsAndHashCode(callSuper = true)
public class EmployeeQuery extends BasePageableQuery {
    private String code;
    private String name;
    private String email;
    private String phone;
    private Boolean isDeleted;
}
