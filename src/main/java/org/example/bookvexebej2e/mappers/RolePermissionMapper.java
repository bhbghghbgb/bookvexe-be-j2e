package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.RolePermissionDbModel;
import org.example.bookvexebej2e.models.dto.role.RolePermissionResponse;
import org.example.bookvexebej2e.models.dto.role.RolePermissionSelectResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.AfterMapping;

@Mapper(componentModel = "spring")
public interface RolePermissionMapper {

    RolePermissionResponse toResponse(RolePermissionDbModel entity);

    RolePermissionSelectResponse toSelectResponse(RolePermissionDbModel entity);

    @AfterMapping
    default void setPermissions(@MappingTarget RolePermissionResponse response, RolePermissionDbModel entity) {
        if (response != null && entity != null) {
            response.setIsDeleted(entity.getIsDeleted());
        }
    }
}
