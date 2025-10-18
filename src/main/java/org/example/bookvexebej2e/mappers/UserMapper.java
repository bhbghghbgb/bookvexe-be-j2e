package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.models.dto.user.UserResponse;
import org.example.bookvexebej2e.models.dto.user.UserSelectResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.AfterMapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(UserDbModel entity);

    UserSelectResponse toSelectResponse(UserDbModel entity);


    @AfterMapping
    default void setPermissions(@MappingTarget UserResponse response, UserDbModel entity) {
        if (response != null && entity != null) {
            response.setIsDeleted(entity.getIsDeleted());
        }
    }
}
