package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.RouteDbModel;
import org.example.bookvexebej2e.models.dto.route.RouteResponse;
import org.example.bookvexebej2e.models.dto.route.RouteSelectResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.AfterMapping;

@Mapper(componentModel = "spring")
public interface RouteMapper {

    RouteResponse toResponse(RouteDbModel entity);

    RouteSelectResponse toSelectResponse(RouteDbModel entity);

    @AfterMapping
    default void setPermissions(@MappingTarget RouteResponse response, RouteDbModel entity) {
        if (response != null && entity != null) {
            response.setIsDeleted(entity.getIsDeleted());
        }
    }
}
