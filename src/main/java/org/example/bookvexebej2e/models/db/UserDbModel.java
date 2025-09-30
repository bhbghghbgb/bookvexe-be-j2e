package org.example.bookvexebej2e.models.db;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class UserDbModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

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

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<RoleUserDbModel> roles = new ArrayList<>();

    @Column(name = "is_active")
    private Boolean isActive = true;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void prePersist() {
        if (userUuid == null) {
            userUuid = UUID.randomUUID();
        }
    }

    private Stream<RoleUserDbModel> getActiveRoleUsers() {
        return roles.stream()
            .filter(roleUser -> roleUser.getIsActive() != null && roleUser.getIsActive());
    }

    public List<String> getRoleCodes() {
        return getActiveRoleUsers().map(roleUser -> roleUser.getRole()
                .getCode())
            .collect(Collectors.toList());
    }

    public boolean hasRole(String roleCode) {
        return getActiveRoleUsers().anyMatch(roleUser -> roleCode.equals(roleUser.getRole()
            .getCode()));
    }
}
