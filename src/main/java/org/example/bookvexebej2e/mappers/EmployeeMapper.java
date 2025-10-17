package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.EmployeeDbModel;
import org.example.bookvexebej2e.models.dto.employee.EmployeeCreate;
import org.example.bookvexebej2e.models.dto.employee.EmployeeResponse;
import org.example.bookvexebej2e.models.dto.employee.EmployeeSelectResponse;
import org.example.bookvexebej2e.models.dto.employee.EmployeeUpdate;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeResponse toResponse(EmployeeDbModel entity);

    EmployeeSelectResponse toSelectResponse(EmployeeDbModel entity);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "isDeleted", ignore = true)
//    @Mapping(target = "createdDate", ignore = true)
//    @Mapping(target = "createdBy", ignore = true)
//    @Mapping(target = "updatedDate", ignore = true)
//    @Mapping(target = "updatedBy", ignore = true)
//    @Mapping(target = "user", ignore = true)
//    @Mapping(target = "carEmployees", ignore = true)
    EmployeeDbModel toEntity(EmployeeCreate createDto);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "isDeleted", ignore = true)
//    @Mapping(target = "createdDate", ignore = true)
//    @Mapping(target = "createdBy", ignore = true)
//    @Mapping(target = "updatedDate", ignore = true)
//    @Mapping(target = "updatedBy", ignore = true)
//    @Mapping(target = "user", ignore = true)
//    @Mapping(target = "carEmployees", ignore = true)
    void updateEntity(EmployeeUpdate updateDto, @MappingTarget EmployeeDbModel entity);
}
