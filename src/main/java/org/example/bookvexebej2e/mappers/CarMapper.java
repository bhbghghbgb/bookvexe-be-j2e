package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.CarDbModel;
import org.example.bookvexebej2e.models.dto.car.CarResponse;
import org.example.bookvexebej2e.models.dto.car.CarSelectResponse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = { CarTypeMapper.class, CarSeatMapper.class })
public interface CarMapper {

    CarResponse toResponse(CarDbModel entity);

    CarSelectResponse toSelectResponse(CarDbModel entity);

    @AfterMapping
    default void setPermissions(@MappingTarget CarResponse response, CarDbModel entity) {
        if (response != null && entity != null) {
            response.setIsDeleted(entity.getIsDeleted());
        }
    }
}
