package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.UserSessionDbModel;
import org.example.bookvexebej2e.models.dto.user.UserSessionResponse;
import org.example.bookvexebej2e.models.dto.user.UserSessionSelectResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.AfterMapping;

@Mapper(componentModel = "spring")
public interface UserSessionMapper {

    UserSessionResponse toResponse(UserSessionDbModel entity);

    UserSessionSelectResponse toSelectResponse(UserSessionDbModel entity);

    @AfterMapping
    default void setPermissions(@MappingTarget UserSessionResponse response, UserSessionDbModel entity) {
        if (response != null && entity != null) {
            response.setIsDeleted(entity.getIsDeleted());
        }
    }
}
