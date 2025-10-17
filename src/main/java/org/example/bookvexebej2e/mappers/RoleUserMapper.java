package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.RoleUserDbModel;
import org.example.bookvexebej2e.models.dto.role.RoleUserCreate;
import org.example.bookvexebej2e.models.dto.role.RoleUserResponse;
import org.example.bookvexebej2e.models.dto.role.RoleUserSelectResponse;
import org.example.bookvexebej2e.models.dto.role.RoleUserUpdate;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleUserMapper {

    RoleUserResponse toResponse(RoleUserDbModel entity);

    RoleUserSelectResponse toSelectResponse(RoleUserDbModel entity);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "isDeleted", ignore = true)
//    @Mapping(target = "createdDate", ignore = true)
//    @Mapping(target = "createdBy", ignore = true)
//    @Mapping(target = "updatedDate", ignore = true)
//    @Mapping(target = "updatedBy", ignore = true)
    RoleUserDbModel toEntity(RoleUserCreate createDto);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "isDeleted", ignore = true)
//    @Mapping(target = "createdDate", ignore = true)
//    @Mapping(target = "createdBy", ignore = true)
//    @Mapping(target = "updatedDate", ignore = true)
//    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(RoleUserUpdate updateDto, @MappingTarget RoleUserDbModel entity);
}
