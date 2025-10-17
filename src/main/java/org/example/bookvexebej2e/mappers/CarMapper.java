package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.CarDbModel;
import org.example.bookvexebej2e.models.dto.car.CarResponse;
import org.example.bookvexebej2e.models.dto.car.CarSelectResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CarMapper {

    @Mapping(target = "carTypeId", source = "carType.id")
    CarResponse toResponse(CarDbModel entity);

    CarSelectResponse toSelectResponse(CarDbModel entity);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "isDeleted", ignore = true)
//    @Mapping(target = "createdDate", ignore = true)
//    @Mapping(target = "createdBy", ignore = true)
//    @Mapping(target = "updatedDate", ignore = true)
//    @Mapping(target = "updatedBy", ignore = true)
//    @Mapping(target = "carSeats", ignore = true)
//    @Mapping(target = "carEmployees", ignore = true)
//    @Mapping(target = "tripCars", ignore = true)
    @Mapping(target = "carType.id", source = "carTypeId")
    CarDbModel toEntity(CarCreate createDto);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "isDeleted", ignore = true)
//    @Mapping(target = "createdDate", ignore = true)
//    @Mapping(target = "createdBy", ignore = true)
//    @Mapping(target = "updatedDate", ignore = true)
//    @Mapping(target = "updatedBy", ignore = true)
//    @Mapping(target = "carSeats", ignore = true)
//    @Mapping(target = "carEmployees", ignore = true)
//    @Mapping(target = "tripCars", ignore = true)
    @Mapping(target = "carType.id", source = "carTypeId")
    void updateEntity(CarUpdate updateDto, @MappingTarget CarDbModel entity);
}
