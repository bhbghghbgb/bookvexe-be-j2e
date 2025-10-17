package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.TripCarDbModel;
import org.example.bookvexebej2e.models.dto.trip.TripCarResponse;
import org.example.bookvexebej2e.models.dto.trip.TripCarSelectResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TripCarMapper {

    TripCarResponse toResponse(TripCarDbModel entity);

    TripCarSelectResponse toSelectResponse(TripCarDbModel entity);
}
