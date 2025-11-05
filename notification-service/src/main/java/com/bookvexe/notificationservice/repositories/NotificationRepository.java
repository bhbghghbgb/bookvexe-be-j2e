package com.bookvexe.notificationservice.repositories;

import com.bookvexe.notificationservice.entities.NotificationDbModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationDbModel, UUID> {

    @Query("SELECT n FROM NotificationDbModel n WHERE n.user = :userId ORDER BY n.createdDate DESC")
    List<NotificationDbModel> findByUserIdOrderByCreatedAtDesc(@Param("userId") UUID userId);

    @Query("SELECT COUNT(n) FROM NotificationDbModel n WHERE n.user = :userId AND n.isRead = false")
    Long countUnreadByUserId(@Param("userId") UUID userId);
}
