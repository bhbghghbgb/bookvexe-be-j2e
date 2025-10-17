package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.RoleDbModel;
import org.example.bookvexebej2e.models.dto.role.RoleResponse;
import org.example.bookvexebej2e.models.dto.role.RoleSelectResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleResponse toResponse(RoleDbModel entity);

    RoleSelectResponse toSelectResponse(RoleDbModel entity);
}
