package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.RoleUserDbModel;
import org.example.bookvexebej2e.models.dto.role.RoleUserResponse;
import org.example.bookvexebej2e.models.dto.role.RoleUserSelectResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleUserMapper {

    RoleUserResponse toResponse(RoleUserDbModel entity);

    RoleUserSelectResponse toSelectResponse(RoleUserDbModel entity);
}
