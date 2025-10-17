package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.dto.car.CarSeatCreate;
import org.example.bookvexebej2e.dto.car.CarSeatResponse;
import org.example.bookvexebej2e.dto.car.CarSeatSelectResponse;
import org.example.bookvexebej2e.dto.car.CarSeatUpdate;
import org.example.bookvexebej2e.models.db.CarSeatDbModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CarSeatMapper {

    CarSeatResponse toResponse(CarSeatDbModel entity);

    CarSeatSelectResponse toSelectResponse(CarSeatDbModel entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "bookingSeats", ignore = true)
    CarSeatDbModel toEntity(CarSeatCreate createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "bookingSeats", ignore = true)
    void updateEntity(CarSeatUpdate updateDto, @MappingTarget CarSeatDbModel entity);
}
