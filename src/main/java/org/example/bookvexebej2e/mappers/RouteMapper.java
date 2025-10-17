package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.dto.route.RouteCreate;
import org.example.bookvexebej2e.dto.route.RouteResponse;
import org.example.bookvexebej2e.dto.route.RouteSelectResponse;
import org.example.bookvexebej2e.dto.route.RouteUpdate;
import org.example.bookvexebej2e.models.db.RouteDbModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RouteMapper {

    RouteResponse toResponse(RouteDbModel entity);

    RouteSelectResponse toSelectResponse(RouteDbModel entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "trips", ignore = true)
    RouteDbModel toEntity(RouteCreate createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "trips", ignore = true)
    void updateEntity(RouteUpdate updateDto, @MappingTarget RouteDbModel entity);
}
