package com.bookvexe.notificationservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDbModel extends BaseModel {
    @Column(length = 255, name = "username", unique = true, nullable = true)
    private String username;

    @Column(length = 255, name = "password")
    private String password;

    @Column(length = 255, name = "isGoogle")
    private Boolean isGoogle;

    @Column(length = 255, name = "googleAccount")
    private String googleAccount;

    @Column(name = "isAdmin")
    private Boolean isAdmin = false;

    @OneToOne
    @JoinColumn(name = "employeeId")
    private EmployeeDbModel employee;

    @OneToOne
    @JoinColumn(name = "customerId")
    private CustomerDbModel customer;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    private List<RoleUserDbModel> roleUsers;

    @OneToMany(mappedBy = "user")
    private List<NotificationDbModel> notifications;
}