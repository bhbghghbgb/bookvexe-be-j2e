package org.example.bookvexebej2e.services.admin;

import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.models.db.TripDbModel;
import org.example.bookvexebej2e.models.requests.TripQueryRequest;
import org.example.bookvexebej2e.repositories.TripRepository;
import org.example.bookvexebej2e.services.admin.base.BaseAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripAdminService extends BaseAdminService<TripDbModel, Integer> {

    private final TripRepository tripRepository;

    @Override
    protected JpaRepository<TripDbModel, Integer> getRepository() {
        return tripRepository;
    }

    public Page<TripDbModel> findTripsByCriteria(TripQueryRequest queryRequest) {
        Pageable pageable = queryRequest.toPageable();

        if (queryRequest.getRouteId() != null) {
            return tripRepository.findByRouteRouteId(queryRequest.getRouteId(), pageable);
        }

        if (queryRequest.getBusId() != null) {
            return tripRepository.findByBusCarId(queryRequest.getBusId(), pageable);
        }

        if (queryRequest.getDepartureAfter() != null && queryRequest.getDepartureBefore() != null) {
            return tripRepository.findByDepartureTimeBetween(queryRequest.getDepartureAfter(), queryRequest.getDepartureBefore(), pageable);
        }

        if (queryRequest.getMinPrice() != null && queryRequest.getMaxPrice() != null) {
            return tripRepository.findByPriceBetween(queryRequest.getMinPrice(), queryRequest.getMaxPrice(), pageable);
        }

        if (queryRequest.getMinSeats() != null) {
            return tripRepository.findByAvailableSeatsGreaterThanEqual(queryRequest.getMinSeats(), pageable);
        }

        if (queryRequest.getDepartureAfter() != null && queryRequest.getMinSeats() != null) {
            return tripRepository.findByDepartureTimeAfterAndAvailableSeatsGreaterThanEqual(
                queryRequest.getDepartureAfter(), queryRequest.getMinSeats(), pageable);
        }

        return tripRepository.findAll(pageable);
    }

    public List<TripDbModel> findFutureTripsWithAvailableSeats(Integer minSeats) {
        return tripRepository.findByDepartureTimeAfterAndAvailableSeatsGreaterThanEqual(
            LocalDateTime.now(), minSeats, Pageable.unpaged()).getContent();
    }
}
