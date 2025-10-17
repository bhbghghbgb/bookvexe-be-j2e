package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.RoleDbModel;
import org.example.bookvexebej2e.models.dto.role.RoleCreate;
import org.example.bookvexebej2e.models.dto.role.RoleResponse;
import org.example.bookvexebej2e.models.dto.role.RoleSelectResponse;
import org.example.bookvexebej2e.models.dto.role.RoleUpdate;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleResponse toResponse(RoleDbModel entity);

    RoleSelectResponse toSelectResponse(RoleDbModel entity);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "isDeleted", ignore = true)
//    @Mapping(target = "createdDate", ignore = true)
//    @Mapping(target = "createdBy", ignore = true)
//    @Mapping(target = "updatedDate", ignore = true)
//    @Mapping(target = "updatedBy", ignore = true)
//    @Mapping(target = "rolePermissions", ignore = true)
//    @Mapping(target = "roleUsers", ignore = true)
    RoleDbModel toEntity(RoleCreate createDto);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "isDeleted", ignore = true)
//    @Mapping(target = "createdDate", ignore = true)
//    @Mapping(target = "createdBy", ignore = true)
//    @Mapping(target = "updatedDate", ignore = true)
//    @Mapping(target = "updatedBy", ignore = true)
//    @Mapping(target = "rolePermissions", ignore = true)
//    @Mapping(target = "roleUsers", ignore = true)
    void updateEntity(RoleUpdate updateDto, @MappingTarget RoleDbModel entity);
}
