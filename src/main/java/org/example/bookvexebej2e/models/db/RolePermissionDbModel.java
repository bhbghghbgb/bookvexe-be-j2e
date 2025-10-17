
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

    @Column(length = 100, name = "Quyền xem chi tiết")
    private Boolean isCanRead;

    @Column(length = 100, name = "Quyền tạo")
    private Boolean isCanCreate;

    @Column(length = 100, name = "Quyền cập nhật")
    private Boolean isCanUpdate;

    @Column(length = 100, name = "Quyền xóa")
    private Boolean isCanDelete;

    @Column(length = 100, name = "Quyền kích hoạt")
    private Boolean isCanActivate;

    @Column(length = 100, name = "Quyền vô hiệu hóa")
    private Boolean isCanDeactivate;

    @Column(length = 100, name = "Quyền nhập khẩu")
    private Boolean isCanImport;

    @Column(length = 100, name = "Quyền xuất khẩu")
    private Boolean isCanExport;
}