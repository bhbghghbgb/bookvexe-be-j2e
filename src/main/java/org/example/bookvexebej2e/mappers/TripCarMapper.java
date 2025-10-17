package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.dto.trip.TripCarCreate;
import org.example.bookvexebej2e.dto.trip.TripCarResponse;
import org.example.bookvexebej2e.dto.trip.TripCarSelectResponse;
import org.example.bookvexebej2e.dto.trip.TripCarUpdate;
import org.example.bookvexebej2e.models.db.TripCarDbModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TripCarMapper {

    TripCarResponse toResponse(TripCarDbModel entity);

    TripCarSelectResponse toSelectResponse(TripCarDbModel entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    TripCarDbModel toEntity(TripCarCreate createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(TripCarUpdate updateDto, @MappingTarget TripCarDbModel entity);
}
