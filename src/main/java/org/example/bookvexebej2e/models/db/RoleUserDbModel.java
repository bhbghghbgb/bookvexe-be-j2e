package org.example.bookvexebej2e.models.db;

import jakarta.persistence.*;
import lombok.*;
import org.example.bookvexebej2e.models.db.embeds.SoftDeleteField;

@Entity
@Table(name = "role_user")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RoleUserDbModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDbModel user;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleDbModel role;

    @Embedded
    private SoftDeleteField softDelete = new SoftDeleteField();

    public Boolean getIsActive() {
        return softDelete != null ? softDelete.getIsActive() : null;
    }

    public void setIsActive(Boolean isActive) {
        if (softDelete == null)
            softDelete = new SoftDeleteField();
        softDelete.setIsActive(isActive);
    }
}
