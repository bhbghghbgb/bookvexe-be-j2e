package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.CarEmployeeDbModel;
import org.example.bookvexebej2e.models.dto.car.CarEmployeeResponse;
import org.example.bookvexebej2e.models.dto.car.CarEmployeeSelectResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.AfterMapping;

@Mapper(componentModel = "spring")
public interface CarEmployeeMapper {

    CarEmployeeResponse toResponse(CarEmployeeDbModel entity);

    CarEmployeeSelectResponse toSelectResponse(CarEmployeeDbModel entity);

    @AfterMapping
    default void setPermissions(@MappingTarget CarEmployeeResponse response, CarEmployeeDbModel entity) {
        if (response != null && entity != null) {
            response.setIsDeleted(entity.getIsDeleted());
        }
    }
}
