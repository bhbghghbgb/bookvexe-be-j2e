package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.dto.role.RolePermissionCreate;
import org.example.bookvexebej2e.dto.role.RolePermissionResponse;
import org.example.bookvexebej2e.dto.role.RolePermissionSelectResponse;
import org.example.bookvexebej2e.dto.role.RolePermissionUpdate;
import org.example.bookvexebej2e.models.db.RolePermissionDbModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RolePermissionMapper {

    RolePermissionResponse toResponse(RolePermissionDbModel entity);

    RolePermissionSelectResponse toSelectResponse(RolePermissionDbModel entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    RolePermissionDbModel toEntity(RolePermissionCreate createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(RolePermissionUpdate updateDto, @MappingTarget RolePermissionDbModel entity);
}
