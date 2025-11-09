package org.example.bookvexebej2e.models.dto.knowledge;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgeSyncRequest {
    private String entityId;      // UUID của entity
    private String entityType;    // "TRIP", "ROUTE", "CAR"
    private String operation;     // "CREATE", "UPDATE", "DELETE"
    private String title;         // Tiêu đề
    private String content;       // Nội dung để embedding
    private String category;      // Loại knowledge
}
