package org.example.bookvexebej2e.repositories.route;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.example.bookvexebej2e.models.db.RouteDbModel;
import org.example.bookvexebej2e.repositories.base.BaseRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteUserRepository extends
        BaseRepository<RouteDbModel>,
        JpaRepository<RouteDbModel, UUID>,
        JpaSpecificationExecutor<RouteDbModel> {

    // Chỉ lấy các tuyến chưa bị xóa (isDeleted = false)
    @Query("SELECT r FROM RouteDbModel r WHERE r.isDeleted = false")
    List<RouteDbModel> findAllNotDeleted();

    @Query("SELECT r FROM RouteDbModel r WHERE r.isDeleted = false")
    List<RouteDbModel> findAllNotDeleted(Sort sort);

    @Query("SELECT r FROM RouteDbModel r WHERE r.id = :id AND r.isDeleted = false")
    Optional<RouteDbModel> findByIdAndNotDeleted(UUID id);

    // Các điểm xuất phát duy nhất
    @Query("SELECT DISTINCT r.startLocation FROM RouteDbModel r WHERE r.isDeleted = false ORDER BY r.startLocation ASC")
    List<String> findDistinctStartLocations();

    // Các điểm đến duy nhất
    @Query("SELECT DISTINCT r.endLocation FROM RouteDbModel r WHERE r.isDeleted = false ORDER BY r.endLocation ASC")
    List<String> findDistinctEndLocations();

    // Lấy điểm đến theo điểm xuất phát
    @Query("""
            SELECT DISTINCT r.endLocation
            FROM RouteDbModel r
            WHERE r.isDeleted = false
              AND LOWER(r.startLocation) = LOWER(:startLocation)
            ORDER BY r.endLocation ASC
            """)
    List<String> findDistinctEndLocationsByStartLocation(String startLocation);
}
