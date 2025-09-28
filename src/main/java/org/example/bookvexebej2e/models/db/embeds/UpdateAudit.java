package org.example.bookvexebej2e.models.db.embeds;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAudit {
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}