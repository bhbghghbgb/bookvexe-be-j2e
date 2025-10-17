package org.example.bookvexebej2e.repository;

import org.example.bookvexebej2e.models.db.CustomerDbModel;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends BaseRepository<CustomerDbModel> {
}
