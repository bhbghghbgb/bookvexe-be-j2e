package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.TripStopDbModel;
import org.example.bookvexebej2e.models.dto.trip.TripStopResponse;
import org.example.bookvexebej2e.models.dto.trip.TripStopSelectResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.AfterMapping;

@Mapper(componentModel = "spring")
public interface TripStopMapper {

    TripStopResponse toResponse(TripStopDbModel entity);

    TripStopSelectResponse toSelectResponse(TripStopDbModel entity);


    @AfterMapping
    default void setPermissions(@MappingTarget TripStopResponse response, TripStopDbModel entity) {
        if (response != null && entity != null) {
            response.setIsDeleted(entity.getIsDeleted());
        }
    }
}
