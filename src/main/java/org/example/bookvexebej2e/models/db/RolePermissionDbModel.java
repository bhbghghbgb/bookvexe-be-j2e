
package org.example.bookvexebej2e.models.db;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "rolePermission")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RolePermissionDbModel extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "roleId")
    private RoleDbModel role;

    private Boolean isCanRead;
    private Boolean isCanCreate;
    private Boolean isCanUpdate;
    private Boolean isCanDelete;
    private Boolean isCanActivate;
    private Boolean isCanDeactivate;
    private Boolean isCanImport;
    private Boolean isCanExport;
}