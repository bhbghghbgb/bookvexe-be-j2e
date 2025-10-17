package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.dto.user.UserSessionCreate;
import org.example.bookvexebej2e.dto.user.UserSessionResponse;
import org.example.bookvexebej2e.dto.user.UserSessionSelectResponse;
import org.example.bookvexebej2e.dto.user.UserSessionUpdate;
import org.example.bookvexebej2e.models.db.UserSessionDbModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserSessionMapper {

    UserSessionResponse toResponse(UserSessionDbModel entity);

    UserSessionSelectResponse toSelectResponse(UserSessionDbModel entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    UserSessionDbModel toEntity(UserSessionCreate createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(UserSessionUpdate updateDto, @MappingTarget UserSessionDbModel entity);
}
