package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.models.dto.user.UserResponse;
import org.example.bookvexebej2e.models.dto.user.UserSelectResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(UserDbModel entity);

    UserSelectResponse toSelectResponse(UserDbModel entity);

}
