package org.example.bookvexebej2e.models.db;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EntityListeners(AuditingEntityListener.class)
public class BaseModel {
    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "isDeleted")
    private Boolean isDeleted = false;

    @CreatedDate
    @Column(updatable = false, name = "createdDate")
    private LocalDateTime createdDate;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, name = "createdBy")
    private UUID createdBy;

    @LastModifiedDate
    @Column(name = "updatedDate")
    private LocalDateTime updatedDate;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, name = "updatedBy")
    private UUID updatedBy;
}
