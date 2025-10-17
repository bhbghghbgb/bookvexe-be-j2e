package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.TripCarDbModel;
import org.example.bookvexebej2e.models.dto.trip.TripCarCreate;
import org.example.bookvexebej2e.models.dto.trip.TripCarResponse;
import org.example.bookvexebej2e.models.dto.trip.TripCarSelectResponse;
import org.example.bookvexebej2e.models.dto.trip.TripCarUpdate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TripCarMapper {

    @Mapping(target = "tripId", source = "trip.id")
    @Mapping(target = "carId", source = "car.id")
    TripCarResponse toResponse(TripCarDbModel entity);

    TripCarSelectResponse toSelectResponse(TripCarDbModel entity);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "isDeleted", ignore = true)
//    @Mapping(target = "createdDate", ignore = true)
//    @Mapping(target = "createdBy", ignore = true)
//    @Mapping(target = "updatedDate", ignore = true)
//    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "trip.id", source = "tripId")
    @Mapping(target = "car.id", source = "carId")
    TripCarDbModel toEntity(TripCarCreate createDto);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "isDeleted", ignore = true)
//    @Mapping(target = "createdDate", ignore = true)
//    @Mapping(target = "createdBy", ignore = true)
//    @Mapping(target = "updatedDate", ignore = true)
//    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "trip.id", source = "tripId")
    @Mapping(target = "car.id", source = "carId")
    void updateEntity(TripCarUpdate updateDto, @MappingTarget TripCarDbModel entity);
}
