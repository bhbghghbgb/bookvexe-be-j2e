package org.example.bookvexebej2e.repository.customer;

import org.example.bookvexebej2e.models.db.CustomerDbModel;
import org.example.bookvexebej2e.repository.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends BaseRepository<CustomerDbModel> {
}
