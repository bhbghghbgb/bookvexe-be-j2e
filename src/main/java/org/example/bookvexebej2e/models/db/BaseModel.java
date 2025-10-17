package org.example.bookvexebej2e.models.db;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
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
public abstract class BaseModel {
    @Id
    @Column(length = 255, name = "uuid")
    private String id;

    @Column(name = "xóa mềm")
    private Boolean isDeleted;

    @CreatedDate
    @Column(updatable = false, name = "Ngày tạo")
    private LocalDateTime createdDate;

    @Column(length = 255, name = "Người tạo")
    private String createdBy;

    @LastModifiedDate
    @Column(name = "Ngày cập nhật")
    private LocalDateTime updatedDate;

    @Column(length = 255, name = "Người cập nhật")
    private String updatedBy;
}