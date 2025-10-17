package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.CarEmployeeDbModel;
import org.example.bookvexebej2e.models.dto.car.CarEmployeeCreate;
import org.example.bookvexebej2e.models.dto.car.CarEmployeeResponse;
import org.example.bookvexebej2e.models.dto.car.CarEmployeeSelectResponse;
import org.example.bookvexebej2e.models.dto.car.CarEmployeeUpdate;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CarEmployeeMapper {

    CarEmployeeResponse toResponse(CarEmployeeDbModel entity);

    CarEmployeeSelectResponse toSelectResponse(CarEmployeeDbModel entity);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "isDeleted", ignore = true)
//    @Mapping(target = "createdDate", ignore = true)
//    @Mapping(target = "createdBy", ignore = true)
//    @Mapping(target = "updatedDate", ignore = true)
//    @Mapping(target = "updatedBy", ignore = true)
    CarEmployeeDbModel toEntity(CarEmployeeCreate createDto);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "isDeleted", ignore = true)
//    @Mapping(target = "createdDate", ignore = true)
//    @Mapping(target = "createdBy", ignore = true)
//    @Mapping(target = "updatedDate", ignore = true)
//    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(CarEmployeeUpdate updateDto, @MappingTarget CarEmployeeDbModel entity);
}
