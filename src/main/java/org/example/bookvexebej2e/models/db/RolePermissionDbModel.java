package org.example.bookvexebej2e.models.db;

import jakarta.persistence.Column;
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

    @Column(length = 100, name = "isCanRead")
    private Boolean isCanRead;

    @Column(length = 100, name = "isCanCreate")
    private Boolean isCanCreate;

    @Column(length = 100, name = "isCanUpdate")
    private Boolean isCanUpdate;

    @Column(length = 100, name = "isCanDelete")
    private Boolean isCanDelete;

    @Column(length = 100, name = "isCanActivate")
    private Boolean isCanActivate;

    @Column(length = 100, name = "isCanDeactivate")
    private Boolean isCanDeactivate;

    @Column(length = 100, name = "isCanImport")
    private Boolean isCanImport;

    @Column(length = 100, name = "isCanExport")
    private Boolean isCanExport;
}