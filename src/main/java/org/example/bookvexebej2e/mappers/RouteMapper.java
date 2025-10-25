package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.RouteDbModel;
import org.example.bookvexebej2e.models.dto.route.RouteResponse;
import org.example.bookvexebej2e.models.dto.route.RouteSelectResponse;
import org.example.bookvexebej2e.models.dto.route.RouteUserResponse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RouteMapper {

    // --- ADMIN / BACKOFFICE ---
    RouteResponse toResponse(RouteDbModel entity);

    RouteSelectResponse toSelectResponse(RouteDbModel entity);

    // --- USER SIDE ---
    RouteUserResponse toUserResponse(RouteDbModel entity);

    // --- AFTER MAPPING HOOK ---
    @AfterMapping
    default void setPermissions(@MappingTarget RouteResponse response, RouteDbModel entity) {
        if (response != null && entity != null) {
            response.setIsDeleted(entity.getIsDeleted());
        }
    }
}
