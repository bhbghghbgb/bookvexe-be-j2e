package com.bookvexe.notificationservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

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