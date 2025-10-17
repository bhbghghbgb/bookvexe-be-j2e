package org.example.bookvexebej2e.models.db;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
