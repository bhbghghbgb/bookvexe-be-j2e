package org.example.bookvexebej2e.models.db;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
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
public abstract class BaseModel {
    @Id
    @Column(length = 255)
    private String id;

    private Boolean isDeleted;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @Column(length = 255)
    private String createdBy;

    @LastModifiedDate
    private LocalDateTime updatedDate;

    @Column(length = 255)
    private String updatedBy;

    @PrePersist
    protected void prePersist() {
        if (this.id == null || this.id.isBlank()) {
            this.id = UUID.randomUUID().toString();
        }
        if (this.isDeleted == null) {
            this.isDeleted = Boolean.FALSE;
        }
    }
}