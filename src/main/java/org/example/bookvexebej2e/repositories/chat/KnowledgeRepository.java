package org.example.bookvexebej2e.repositories.chat;

import org.example.bookvexebej2e.models.db.Knowledge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface KnowledgeRepository extends JpaRepository<Knowledge, Long> {

    @Query(value = """
        SELECT * FROM knowledge
        ORDER BY embedding <-> CAST(:queryEmbedding AS vector)
        LIMIT :limit
        """, nativeQuery = true)
    List<Knowledge> findSimilar(@Param("queryEmbedding") String queryEmbedding, @Param("limit") int limit);
}
