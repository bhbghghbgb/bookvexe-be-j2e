package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.RoleUserDbModel;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.models.responses.RoleDto;
import org.example.bookvexebej2e.models.responses.RoleUserDto;
import org.example.bookvexebej2e.models.responses.UserDto;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(UserDbModel userDbModel);

    default UserDto toDtoWithRoles(UserDbModel user) {
        if (user == null) {
            return null;
        }

        UserDto dto = new UserDto();
        // Map simple fields
        dto.setUserId(user.getUserId());
        dto.setUserUuid(user.getUserUuid());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setIsActive(user.getIsActive());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());

        // Manually map RoleUserDbModel to RoleUserDto
        if (user.getRoles() != null) {
            List<RoleUserDto> roleUserDtos = user.getRoles()
                .stream()
                .map(this::mapToRoleUserDto)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
            dto.setRoles(roleUserDtos);
        } else {
            dto.setRoles(new ArrayList<>());
        }

        return dto;
    }

    default RoleUserDto mapToRoleUserDto(RoleUserDbModel roleUser) {
        if (roleUser == null) {
            return null;
        }

        RoleUserDto dto = new RoleUserDto();
        dto.setId(roleUser.getId());
        dto.setIsActive(roleUser.getIsActive());

        // Map the RoleDbModel to RoleDto
        if (roleUser.getRole() != null) {
            RoleDto roleDto = new RoleDto();
            roleDto.setId(roleUser.getRole()
                .getId());
            roleDto.setCode(roleUser.getRole()
                .getCode());
            roleDto.setName(roleUser.getRole()
                .getName());
            roleDto.setCreatedAt(roleUser.getRole()
                .getCreatedAt());
            dto.setRole(roleDto);
        }

        return dto;
    }

}