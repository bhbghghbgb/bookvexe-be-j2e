package org.example.bookvexebej2e.models.db;

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
    @Column(length = 255, name = "username", unique = true)
    private String username;

    @Column(length = 255, name = "password")
    private String password;

    @Column(length = 255, name = "isGoogle")
    private Boolean isGoogle;

    @Column(length = 255, name = "googleAccount")
    private String googleAccount;

    @OneToOne
    @JoinColumn(name = "employeeId")
    private EmployeeDbModel employee;

    @OneToOne
    @JoinColumn(name = "customerId")
    private CustomerDbModel customer;

    @OneToMany(mappedBy = "user")
    private List<RoleUserDbModel> roleUsers;

    @OneToMany(mappedBy = "user")
    private List<BookingDbModel> bookings;

    @OneToMany(mappedBy = "user")
    private List<NotificationDbModel> notifications;
}