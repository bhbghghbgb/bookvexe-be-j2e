package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.EmployeeDbModel;
import org.example.bookvexebej2e.models.db.RoleUserDbModel;
import org.example.bookvexebej2e.models.dto.employee.EmployeeResponse;
import org.example.bookvexebej2e.models.dto.employee.EmployeeSelectResponse;
import org.example.bookvexebej2e.models.dto.role.RoleResponse;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "roles", source = "user.roleUsers", qualifiedByName = "mapRoleUsersToRoles")
    EmployeeResponse toResponse(EmployeeDbModel entity);

    EmployeeSelectResponse toSelectResponse(EmployeeDbModel entity);

    @Named("mapRoleUsersToRoles")
    default List<RoleResponse> mapRoleUsersToRoles(List<RoleUserDbModel> roleUsers) {
        if (roleUsers == null) {
            return List.of();
        }
        return roleUsers.stream()
            .map(roleUser -> {
                RoleResponse roleResponse = new RoleResponse();
                roleResponse.setId(roleUser.getRole().getId());
                roleResponse.setCode(roleUser.getRole().getCode());
                roleResponse.setName(roleUser.getRole().getName());
                return roleResponse;
            })
            .collect(Collectors.toList());
    }

    @AfterMapping
    default void setPermissions(@MappingTarget EmployeeResponse response, EmployeeDbModel entity) {
        if (response != null && entity != null) {
            response.setIsDeleted(entity.getIsDeleted());
        }
    }
}
