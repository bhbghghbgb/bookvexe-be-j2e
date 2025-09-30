package org.example.bookvexebej2e.models.db;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "is_active")
    private Boolean isActive = true;
}
