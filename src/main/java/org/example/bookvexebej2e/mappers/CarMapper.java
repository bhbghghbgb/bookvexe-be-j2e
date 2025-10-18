package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.CarDbModel;
import org.example.bookvexebej2e.models.dto.car.CarResponse;
import org.example.bookvexebej2e.models.dto.car.CarSelectResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { CarTypeMapper.class, CarSeatMapper.class })
public interface CarMapper {

    CarResponse toResponse(CarDbModel entity);

    CarSelectResponse toSelectResponse(CarDbModel entity);

}
