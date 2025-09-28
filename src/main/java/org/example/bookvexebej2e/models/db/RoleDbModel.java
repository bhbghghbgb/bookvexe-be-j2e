package org.example.bookvexebej2e.models.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.bookvexebej2e.models.db.embeds.CreateAudit;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "role")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class RoleDbModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "code", unique = true, nullable = false, length = 100)
    private String code;

    @Column(name = "name", length = 100)
    private String name;

    @Embedded
    private CreateAudit createAudit = new CreateAudit();

    // Convenience getters
    public LocalDateTime getCreatedAt() {
        return createAudit != null ? createAudit.getCreatedAt() : null;
    }
}