package org.example.bookvexebej2e.repositories;

import org.example.bookvexebej2e.models.db.NotificationTypeDbModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// NotificationType Repository
@Repository
public interface NotificationTypeRepository extends JpaRepository<NotificationTypeDbModel, Integer> {
    Optional<NotificationTypeDbModel> findByTypeName(String typeName);
}