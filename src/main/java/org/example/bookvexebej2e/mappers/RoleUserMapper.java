package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.RoleUserDbModel;
import org.example.bookvexebej2e.models.responses.RoleUserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface RoleUserMapper {
    // Map the fields from RoleUserDbModel to RoleUserDto
    RoleUserDto toDto(RoleUserDbModel roleUserDbModel);
}