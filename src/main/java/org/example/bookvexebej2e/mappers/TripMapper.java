package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.dto.trip.TripCreate;
import org.example.bookvexebej2e.dto.trip.TripResponse;
import org.example.bookvexebej2e.dto.trip.TripSelectResponse;
import org.example.bookvexebej2e.dto.trip.TripUpdate;
import org.example.bookvexebej2e.models.db.TripDbModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TripMapper {

    TripResponse toResponse(TripDbModel entity);

    TripSelectResponse toSelectResponse(TripDbModel entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "tripStops", ignore = true)
    @Mapping(target = "tripCars", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "notifications", ignore = true)
    TripDbModel toEntity(TripCreate createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "tripStops", ignore = true)
    @Mapping(target = "tripCars", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "notifications", ignore = true)
    void updateEntity(TripUpdate updateDto, @MappingTarget TripDbModel entity);
}
