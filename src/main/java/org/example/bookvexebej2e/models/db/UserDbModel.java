package org.example.bookvexebej2e.models.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.bookvexebej2e.models.db.embeds.CreateAudit;
import org.example.bookvexebej2e.models.db.embeds.SoftDeleteField;
import org.example.bookvexebej2e.models.db.embeds.UpdateAudit;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class UserDbModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "user_uuid", unique = true, columnDefinition = "BINARY(16)")
    private UUID userUuid;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "phone_number", unique = true, nullable = false, length = 15)
    private String phoneNumber;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleDbModel role;

    @Embedded
    private SoftDeleteField softDelete = new SoftDeleteField();

    @Embedded
    private CreateAudit createAudit = new CreateAudit();

    @Embedded
    private UpdateAudit updateAudit = new UpdateAudit();

    @PrePersist
    protected void prePersist() {
        if (userUuid == null) {
            userUuid = UUID.randomUUID();
        }
        if (softDelete == null || softDelete.getIsActive() == null) {
            softDelete = new SoftDeleteField();
        }
    }

    // Convenience getters
    public LocalDateTime getCreatedAt() {
        return createAudit != null ? createAudit.getCreatedAt() : null;
    }

    public LocalDateTime getUpdatedAt() {
        return updateAudit != null ? updateAudit.getUpdatedAt() : null;
    }

    public Boolean getIsActive() {
        return softDelete != null ? softDelete.getIsActive() : null;
    }

    public void setIsActive(Boolean isActive) {
        if (softDelete == null)
            softDelete = new SoftDeleteField();
        softDelete.setIsActive(isActive);
    }
}
