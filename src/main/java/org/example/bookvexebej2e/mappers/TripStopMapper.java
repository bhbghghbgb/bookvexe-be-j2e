package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.TripStopDbModel;
import org.example.bookvexebej2e.models.dto.trip.TripStopResponse;
import org.example.bookvexebej2e.models.dto.trip.TripStopSelectResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TripStopMapper {

    @Mapping(target = "tripId", source = "trip.id")
    TripStopResponse toResponse(TripStopDbModel entity);

    TripStopSelectResponse toSelectResponse(TripStopDbModel entity);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "isDeleted", ignore = true)
//    @Mapping(target = "createdDate", ignore = true)
//    @Mapping(target = "createdBy", ignore = true)
//    @Mapping(target = "updatedDate", ignore = true)
//    @Mapping(target = "updatedBy", ignore = true)
//    @Mapping(target = "pickupBookings", ignore = true)
//    @Mapping(target = "dropoffBookings", ignore = true)
    @Mapping(target = "trip.id", source = "tripId")
    TripStopDbModel toEntity(TripStopCreate createDto);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "isDeleted", ignore = true)
//    @Mapping(target = "createdDate", ignore = true)
//    @Mapping(target = "createdBy", ignore = true)
//    @Mapping(target = "updatedDate", ignore = true)
//    @Mapping(target = "updatedBy", ignore = true)
//    @Mapping(target = "pickupBookings", ignore = true)
//    @Mapping(target = "dropoffBookings", ignore = true)
    @Mapping(target = "trip.id", source = "tripId")
    void updateEntity(TripStopUpdate updateDto, @MappingTarget TripStopDbModel entity);
}
