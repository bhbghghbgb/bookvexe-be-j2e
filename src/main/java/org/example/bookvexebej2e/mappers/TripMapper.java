package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.TripDbModel;
import org.example.bookvexebej2e.models.dto.trip.TripResponse;
import org.example.bookvexebej2e.models.dto.trip.TripSelectResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TripMapper {

    TripResponse toResponse(TripDbModel entity);

    TripSelectResponse toSelectResponse(TripDbModel entity);

}
