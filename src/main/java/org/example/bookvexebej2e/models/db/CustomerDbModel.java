package org.example.bookvexebej2e.models.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "customer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerDbModel extends BaseModel {
    @Column(length = 255, name = "code")
    private String code;

    @Column(length = 255, name = "name")
    private String name;

    @Column(length = 100, name = "email")
    private String email;

    @Column(length = 15, name = "phone")
    private String phone;

    @ManyToOne
    @JoinColumn(name = "customerTypeId")
    private CustomerTypeDbModel customerType;

    @Column(columnDefinition = "TEXT", name = "description")
    private String description;

    @OneToOne(mappedBy = "customer")
    private UserDbModel user;
}