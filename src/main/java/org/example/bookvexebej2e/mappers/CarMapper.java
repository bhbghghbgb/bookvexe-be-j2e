package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.CarDbModel;
import org.example.bookvexebej2e.models.dto.car.CarCreate;
import org.example.bookvexebej2e.models.dto.car.CarResponse;
import org.example.bookvexebej2e.models.dto.car.CarSelectResponse;
import org.example.bookvexebej2e.models.dto.car.CarUpdate;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CarMapper {

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
    void updateEntity(CarUpdate updateDto, @MappingTarget CarDbModel entity);
}
