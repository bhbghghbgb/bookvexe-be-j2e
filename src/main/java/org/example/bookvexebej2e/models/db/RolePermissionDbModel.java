package org.example.bookvexebej2e.models.db;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "role_permission")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionDbModel {
    @Id
    @Column(name = "role_id")
    private Integer roleId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "role_id")
    private RoleDbModel role;

    @Column(name = "canRead")
    private Boolean canRead;

    @Column(name = "canCreate")
    private Boolean canCreate;

    @Column(name = "canUpdate")
    private Boolean canUpdate;

    @Column(name = "canDelete")
    private Boolean canDelete;

    @Column(name = "canActivate")
    private Boolean canActivate;

    @Column(name = "canDeactivate")
    private Boolean canDeactivate;

    @Column(name = "canImport")
    private Boolean canImport;

    @Column(name = "canExport")
    private Boolean canExport;
}