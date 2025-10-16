package org.example.bookvexebej2e.models.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleUserDto {
    private Integer id;
    private RoleDto role;
    private Boolean isActive;
}