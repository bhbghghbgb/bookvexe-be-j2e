package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.RouteDbModel;
import org.example.bookvexebej2e.models.dto.route.RouteResponse;
import org.example.bookvexebej2e.models.dto.route.RouteSelectResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RouteMapper {

    RouteResponse toResponse(RouteDbModel entity);

    RouteSelectResponse toSelectResponse(RouteDbModel entity);
}
