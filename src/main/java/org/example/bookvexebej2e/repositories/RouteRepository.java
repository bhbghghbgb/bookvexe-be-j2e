package org.example.bookvexebej2e.repositories;

import org.example.bookvexebej2e.models.db.RouteDbModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<RouteDbModel, String>, JpaSpecificationExecutor<RouteDbModel> {
    // All queries are handled by JpaSpecificationExecutor with dynamic Specification in RouteAdminService
}
