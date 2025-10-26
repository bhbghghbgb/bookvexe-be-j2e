package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.TripDbModel;
import org.example.bookvexebej2e.models.dto.trip.TripUserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { TripCarMapper.class, RouteMapper.class })
public interface TripUserMapper {

    @Mapping(source = "tripCars", target = "tripCars")
    TripUserResponse toUserResponse(TripDbModel entity);
}