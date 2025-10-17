package org.example.bookvexebej2e.models.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
    @Column(length = 100, unique = true, name = "Mã vai trò")
    private String code;

    @Column(length = 100, name = "Tên vai trò")
    private String name;

    @Column(columnDefinition = "TEXT", name = "Mô tả")
    private String description;

    @OneToMany(mappedBy = "role")
    private List<RolePermissionDbModel> rolePermissions;

    @OneToMany(mappedBy = "role")
    private List<RoleUserDbModel> roleUsers;
}