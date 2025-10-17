
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
@Table(name = "roleUser")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoleUserDbModel extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "roleId")
    private RoleDbModel role;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserDbModel user;
}