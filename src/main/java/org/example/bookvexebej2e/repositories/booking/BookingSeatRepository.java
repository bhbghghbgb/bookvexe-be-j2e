package org.example.bookvexebej2e.repositories.booking;

import org.example.bookvexebej2e.models.db.BookingSeatDbModel;
import org.example.bookvexebej2e.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingSeatRepository extends BaseRepository<BookingSeatDbModel> {

    /**
     * Count booking seats with code starting with the given prefix
     * 
     * @param codePrefix The code prefix to search for
     * @return Number of booking seats with codes starting with the prefix
     */
    @Query("SELECT COUNT(bs) FROM BookingSeatDbModel bs WHERE bs.code LIKE CONCAT(:codePrefix, '%')")
    long countByCodeStartingWith(@Param("codePrefix") String codePrefix);
}
