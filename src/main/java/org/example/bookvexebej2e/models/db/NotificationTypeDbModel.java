package org.example.bookvexebej2e.models.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "notificationTypes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NotificationTypeDbModel extends BaseModel {
    @Column(length = 255, unique = true, name = "Mã loại thông báo")
    private String code;

    @Column(length = 50, unique = true, name = "Tên loại thông báo")
    private String name;

    @Column(length = 255, name = "Mô tả")
    private String description;

    @OneToMany(mappedBy = "type")
    private List<NotificationDbModel> notifications;
}
