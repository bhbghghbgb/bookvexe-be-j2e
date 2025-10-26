package org.example.bookvexebej2e.repositories.booking;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.example.bookvexebej2e.models.db.BookingDbModel;
import org.example.bookvexebej2e.models.db.CustomerDbModel;
import org.example.bookvexebej2e.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingUserRepository extends BaseRepository<BookingDbModel> {

    // Lấy tất cả booking của một customer chưa bị xóa
    List<BookingDbModel> findByCustomerAndIsDeletedFalse(CustomerDbModel customer);

    // Lấy 1 booking cụ thể chưa bị xóa
    Optional<BookingDbModel> findByIdAndIsDeletedFalse(UUID id);
}
