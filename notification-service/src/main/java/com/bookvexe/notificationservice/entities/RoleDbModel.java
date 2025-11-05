package com.bookvexe.notificationservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoleDbModel extends BaseModel {
    @Column(length = 100, unique = true, name = "code")
    private String code;

    @Column(length = 100, name = "name")
    private String name;

    @Column(columnDefinition = "TEXT", name = "description")
    private String description;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RolePermissionDbModel> rolePermissions;

    @OneToMany(mappedBy = "role")
    private List<RoleUserDbModel> roleUsers;
}