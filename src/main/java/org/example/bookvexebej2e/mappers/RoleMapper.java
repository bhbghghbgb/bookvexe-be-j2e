package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.RoleDbModel;
import org.example.bookvexebej2e.models.responses.RoleDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDto toDto(RoleDbModel roleDbModel);
}
