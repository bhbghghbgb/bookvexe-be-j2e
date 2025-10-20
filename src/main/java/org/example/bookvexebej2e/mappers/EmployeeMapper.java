package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.EmployeeDbModel;
import org.example.bookvexebej2e.models.dto.employee.EmployeeResponse;
import org.example.bookvexebej2e.models.dto.employee.EmployeeSelectResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeResponse toResponse(EmployeeDbModel entity);

    EmployeeSelectResponse toSelectResponse(EmployeeDbModel entity);

}
