package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.models.dto.user.UserCreate;
import org.example.bookvexebej2e.models.dto.user.UserResponse;
import org.example.bookvexebej2e.models.dto.user.UserSelectResponse;
import org.example.bookvexebej2e.models.dto.user.UserUpdate;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(UserDbModel entity);

    UserSelectResponse toSelectResponse(UserDbModel entity);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "isDeleted", ignore = true)
//    @Mapping(target = "createdDate", ignore = true)
//    @Mapping(target = "createdBy", ignore = true)
//    @Mapping(target = "updatedDate", ignore = true)
//    @Mapping(target = "updatedBy", ignore = true)
//    @Mapping(target = "roleUsers", ignore = true)
//    @Mapping(target = "bookings", ignore = true)
//    @Mapping(target = "notifications", ignore = true)
    UserDbModel toEntity(UserCreate createDto);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "isDeleted", ignore = true)
//    @Mapping(target = "createdDate", ignore = true)
//    @Mapping(target = "createdBy", ignore = true)
//    @Mapping(target = "updatedDate", ignore = true)
//    @Mapping(target = "updatedBy", ignore = true)
//    @Mapping(target = "roleUsers", ignore = true)
//    @Mapping(target = "bookings", ignore = true)
//    @Mapping(target = "notifications", ignore = true)
    void updateEntity(UserUpdate updateDto, @MappingTarget UserDbModel entity);
}
