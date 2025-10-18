package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.RoleDbModel;
import org.example.bookvexebej2e.models.dto.role.RoleResponse;
import org.example.bookvexebej2e.models.dto.role.RoleSelectResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.AfterMapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleResponse toResponse(RoleDbModel entity);

    RoleSelectResponse toSelectResponse(RoleDbModel entity);

    @AfterMapping
    default void setPermissions(@MappingTarget RoleResponse response, RoleDbModel entity) {
        if (response != null && entity != null) {
            response.setIsDeleted(entity.getIsDeleted());
        }
    }
}
