package org.example.bookvexebej2e.repository.employee;

import org.example.bookvexebej2e.models.db.EmployeeDbModel;
import org.example.bookvexebej2e.repository.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends BaseRepository<EmployeeDbModel> {
}
