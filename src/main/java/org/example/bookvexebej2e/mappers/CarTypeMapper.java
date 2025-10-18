package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.CarTypeDbModel;
import org.example.bookvexebej2e.models.dto.car.CarTypeResponse;
import org.example.bookvexebej2e.models.dto.car.CarTypeSelectResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.AfterMapping;

@Mapper(componentModel = "spring")
public interface CarTypeMapper {

    CarTypeResponse toResponse(CarTypeDbModel entity);

    CarTypeSelectResponse toSelectResponse(CarTypeDbModel entity);


    @AfterMapping
    default void setPermissions(@MappingTarget CarTypeResponse response, CarTypeDbModel entity) {
        if (response != null && entity != null) {
            response.setIsDeleted(entity.getIsDeleted());
        }
    }
}
