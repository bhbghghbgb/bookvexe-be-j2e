package org.example.bookvexebej2e.services.admin;

import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.models.db.BookingDbModel;
import org.example.bookvexebej2e.models.db.BookingSeatDbModel;
import org.example.bookvexebej2e.models.db.TripDbModel;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.models.requests.BookingQueryRequest;
import org.example.bookvexebej2e.repositories.BookingRepository;
import org.example.bookvexebej2e.repositories.BookingSeatRepository;
import org.example.bookvexebej2e.repositories.TripRepository;
import org.example.bookvexebej2e.repositories.UserRepository;
import org.example.bookvexebej2e.services.admin.base.BaseAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingAdminService extends BaseAdminService<BookingDbModel, Integer> {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final BookingSeatRepository bookingSeatRepository;

    @Override
    protected JpaRepository<BookingDbModel, Integer> getRepository() {
        return bookingRepository;
    }

    public Page<BookingDbModel> findBookingsByCriteria(BookingQueryRequest queryRequest) {
        Pageable pageable = queryRequest.toPageable();

        if (queryRequest.getUserId() != null && queryRequest.getTripId() != null) {
            Optional<UserDbModel> user = userRepository.findById(queryRequest.getUserId());
            Optional<TripDbModel> trip = tripRepository.findById(queryRequest.getTripId());
            if (user.isPresent() && trip.isPresent()) {
                return bookingRepository.findByUserAndBookingStatus(user.get(), queryRequest.getStatus(), pageable);
            }
        }

        if (queryRequest.getUserId() != null) {
            return bookingRepository.findByUserUserId(queryRequest.getUserId(), pageable);
        }

        if (queryRequest.getTripId() != null) {
            return bookingRepository.findByTripTripId(queryRequest.getTripId(), pageable);
        }

        if (queryRequest.getStatus() != null) {
            return bookingRepository.findByBookingStatus(queryRequest.getStatus(), pageable);
        }

        if (queryRequest.getStatuses() != null && !queryRequest.getStatuses()
            .isEmpty()) {
            return bookingRepository.findByBookingStatusIn(queryRequest.getStatuses(), pageable);
        }

        if (queryRequest.getCreatedAfter() != null && queryRequest.getCreatedBefore() != null) {
            return bookingRepository.findByCreatedAtBetween(queryRequest.getCreatedAfter(),
                queryRequest.getCreatedBefore(), pageable);
        }

        if (queryRequest.getMinPrice() != null && queryRequest.getMaxPrice() != null) {
            return bookingRepository.findByTotalPriceBetween(queryRequest.getMinPrice(), queryRequest.getMaxPrice(),
                pageable);
        }

        return bookingRepository.findAll(pageable);
    }

    public List<BookingSeatDbModel> getBookingSeats(Integer bookingId) {
        return bookingRepository.findById(bookingId)
            .map(bookingSeatRepository::findByBooking)
            .orElse(Collections.emptyList());
    }
}
