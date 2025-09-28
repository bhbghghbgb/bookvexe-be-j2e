package org.example.bookvexebej2e.models.db;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notification_types")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NotificationTypeDbModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id")
    private Integer typeId;

    @Column(name = "type_name", unique = true, nullable = false, length = 50)
    private String typeName;

    @Column(name = "description", length = 255)
    private String description;
}
