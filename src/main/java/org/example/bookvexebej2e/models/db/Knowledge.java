package org.example.bookvexebej2e.models.db;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;
import java.util.Arrays;

@Entity
@Table(name = "knowledge")
@Data
public class Knowledge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "text")
    private String content;

    @Column(columnDefinition = "vector(1536)")
    private float[] embedding;

    @Column(name = "created_at")
    private Instant createdAt;

    @Override
    public String toString() {
        return title + ": " + (content.length() > 100 ? content.substring(0, 100) + "..." : content);
    }
}
