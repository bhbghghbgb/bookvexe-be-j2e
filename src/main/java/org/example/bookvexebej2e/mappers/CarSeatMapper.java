package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.CarSeatDbModel;
import org.example.bookvexebej2e.models.dto.car.CarSeatResponse;
import org.example.bookvexebej2e.models.dto.car.CarSeatSelectResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CarSeatMapper {

    CarSeatResponse toResponse(CarSeatDbModel entity);

    CarSeatSelectResponse toSelectResponse(CarSeatDbModel entity);

}
