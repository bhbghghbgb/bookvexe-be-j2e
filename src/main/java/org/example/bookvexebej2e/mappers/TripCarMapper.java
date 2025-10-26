package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.TripCarDbModel;
import org.example.bookvexebej2e.models.dto.trip.TripCarResponse;
import org.example.bookvexebej2e.models.dto.trip.TripCarSelectResponse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = { CarMapper.class })
public interface TripCarMapper {

    @Mapping(source = "trip.id", target = "tripId")
    @Mapping(source = "car.id", target = "carId")
    TripCarResponse toResponse(TripCarDbModel entity);

    TripCarSelectResponse toSelectResponse(TripCarDbModel entity);

    @AfterMapping
    default void setPermissions(@MappingTarget TripCarResponse response, TripCarDbModel entity) {
        if (response != null && entity != null) {
            response.setIsDeleted(entity.getIsDeleted());
        }
    }
}