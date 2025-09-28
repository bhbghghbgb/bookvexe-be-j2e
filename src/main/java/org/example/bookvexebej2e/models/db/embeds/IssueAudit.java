package org.example.bookvexebej2e.models.db.embeds;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueAudit {
    @CreatedDate
    @Column(name = "issued_at")
    private LocalDateTime issuedAt;
}
