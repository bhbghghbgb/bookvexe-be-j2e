package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.RolePermissionDbModel;
import org.example.bookvexebej2e.models.dto.role.RolePermissionResponse;
import org.example.bookvexebej2e.models.dto.role.RolePermissionSelectResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RolePermissionMapper {

    RolePermissionResponse toResponse(RolePermissionDbModel entity);

    RolePermissionSelectResponse toSelectResponse(RolePermissionDbModel entity);
}
