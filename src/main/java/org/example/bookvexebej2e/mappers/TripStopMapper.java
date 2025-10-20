package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.TripStopDbModel;
import org.example.bookvexebej2e.models.dto.trip.TripStopResponse;
import org.example.bookvexebej2e.models.dto.trip.TripStopSelectResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TripStopMapper {

    TripStopResponse toResponse(TripStopDbModel entity);

    TripStopSelectResponse toSelectResponse(TripStopDbModel entity);

}
