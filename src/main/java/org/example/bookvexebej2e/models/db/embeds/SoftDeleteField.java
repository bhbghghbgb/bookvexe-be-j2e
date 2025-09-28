package org.example.bookvexebej2e.models.db.embeds;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoftDeleteField {
    @Column(name = "is_active")
    private Boolean isActive = true;
}