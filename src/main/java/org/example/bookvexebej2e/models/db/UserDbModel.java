
package org.example.bookvexebej2e.models.db;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDbModel extends BaseModel {
    @Column(length = 255, name = "Tên đăng nhập", unique = true)
    private String username;

    @Column(length = 255, name = "Mật khẩu")
    private String password;

    @Column(length = 255, name = "Đăng nhập bằng google")
    private Boolean isGoogle;

    @Column(length = 255, name = "Tài khoản Google")
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