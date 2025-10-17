package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.CarEmployeeDbModel;
import org.example.bookvexebej2e.models.dto.car.CarEmployeeResponse;
import org.example.bookvexebej2e.models.dto.car.CarEmployeeSelectResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CarEmployeeMapper {

    @Mapping(target = "carId", source = "car.id")
    @Mapping(target = "employeeId", source = "employee.id")
    CarEmployeeResponse toResponse(CarEmployeeDbModel entity);

    CarEmployeeSelectResponse toSelectResponse(CarEmployeeDbModel entity);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "isDeleted", ignore = true)
//    @Mapping(target = "createdDate", ignore = true)
//    @Mapping(target = "createdBy", ignore = true)
//    @Mapping(target = "updatedDate", ignore = true)
//    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "car.id", source = "carId")
    @Mapping(target = "employee.id", source = "employeeId")
    CarEmployeeDbModel toEntity(CarEmployeeCreate createDto);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "isDeleted", ignore = true)
//    @Mapping(target = "createdDate", ignore = true)
//    @Mapping(target = "createdBy", ignore = true)
//    @Mapping(target = "updatedDate", ignore = true)
//    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "car.id", source = "carId")
    @Mapping(target = "employee.id", source = "employeeId")
    void updateEntity(CarEmployeeUpdate updateDto, @MappingTarget CarEmployeeDbModel entity);
}
