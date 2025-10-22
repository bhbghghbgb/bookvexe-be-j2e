package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.RoleUserDbModel;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.models.dto.role.RoleResponse;
import org.example.bookvexebej2e.models.dto.user.UserResponse;
import org.example.bookvexebej2e.models.dto.user.UserSelectResponse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public abstract class UserMapper {

    @Autowired
    protected RoleMapper roleMapper;

    @Mapping(target = "roles", expression = "java(mapRoles(entity.getRoleUsers()))")
    public abstract UserResponse toResponse(UserDbModel entity);

    public abstract UserSelectResponse toSelectResponse(UserDbModel entity);

    protected List<RoleResponse> mapRoles(List<RoleUserDbModel> roleUsers) {
        if (roleUsers == null) {
            return Collections.emptyList();
        }
        return roleUsers.stream()
            .map(RoleUserDbModel::getRole)
            .filter(Objects::nonNull)
            .map(roleMapper::toResponse)
            .collect(Collectors.toList());
    }

    @AfterMapping
    protected void setPermissions(@MappingTarget UserResponse response, UserDbModel entity) {
        if (response != null && entity != null) {
            response.setIsDeleted(entity.getIsDeleted());
        }
    }
}
