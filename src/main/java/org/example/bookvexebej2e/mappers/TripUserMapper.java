package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.TripDbModel;
import org.example.bookvexebej2e.models.dto.trip.TripUserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { RouteMapper.class, TripStopMapper.class, TripCarMapper.class })
public interface TripUserMapper {

    TripUserResponse toUserResponse(TripDbModel entity);

}