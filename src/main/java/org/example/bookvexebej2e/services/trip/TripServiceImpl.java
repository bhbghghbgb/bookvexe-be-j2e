package org.example.bookvexebej2e.services.trip;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.exceptions.ResourceNotFoundException;
import org.example.bookvexebej2e.mappers.TripMapper;
import org.example.bookvexebej2e.models.db.CarDbModel;
import org.example.bookvexebej2e.models.db.RouteDbModel;
import org.example.bookvexebej2e.models.db.TripCarDbModel;
import org.example.bookvexebej2e.models.db.TripDbModel;
import org.example.bookvexebej2e.models.dto.trip.TripCreate;
import org.example.bookvexebej2e.models.dto.trip.TripQuery;
import org.example.bookvexebej2e.models.dto.trip.TripResponse;
import org.example.bookvexebej2e.models.dto.trip.TripSelectResponse;
import org.example.bookvexebej2e.models.dto.trip.TripUpdate;
import org.example.bookvexebej2e.repositories.car.CarRepository;
import org.example.bookvexebej2e.repositories.route.RouteRepository;
import org.example.bookvexebej2e.repositories.trip.TripCarRepository;
import org.example.bookvexebej2e.repositories.trip.TripRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final RouteRepository routeRepository;
    private final TripMapper tripMapper;
    private final TripCarRepository tripCarRepository;
    private final CarRepository carRepository;

    @Override
    public List<TripResponse> findAll() {
        List<TripDbModel> entities = tripRepository.findAllNotDeleted();
        return entities.stream()
                .map(tripMapper::toResponse)
                .toList();
    }

    @Override
    public Page<TripResponse> findAll(TripQuery query) {
        Specification<TripDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<TripDbModel> entities = tripRepository.findAll(spec, pageable);
        return entities.map(tripMapper::toResponse);
    }

    @Override
    public TripResponse findById(UUID id) {
        TripDbModel entity = tripRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException(TripDbModel.class, id));
        return tripMapper.toResponse(entity);
    }

    @Override
    public TripResponse create(TripCreate createDto) {
        TripDbModel entity = new TripDbModel();
        entity.setDepartureTime(createDto.getDepartureTime());
        entity.setPrice(createDto.getPrice());
        entity.setAvailableSeats(createDto.getAvailableSeats());

        RouteDbModel route = routeRepository.findById(createDto.getRouteId())
                .orElseThrow(() -> new ResourceNotFoundException(RouteDbModel.class, createDto.getRouteId()));
        entity.setRoute(route);

        TripDbModel savedEntity = tripRepository.save(entity);

        // Tạo TripCar từ danh sách carIds đơn giản
        if (createDto.getCarIds() != null && !createDto.getCarIds().isEmpty()) {
            for (UUID carId : createDto.getCarIds()) {
                CarDbModel car = carRepository.findById(carId)
                        .orElseThrow(() -> new ResourceNotFoundException(CarDbModel.class, carId));

                TripCarDbModel tripCar = new TripCarDbModel();
                tripCar.setTrip(savedEntity);
                tripCar.setCar(car);
                // Sử dụng giá và số ghế từ trip làm mặc định
                tripCar.setPrice(createDto.getPrice());
                tripCar.setAvailableSeats(createDto.getAvailableSeats());

                tripCarRepository.save(tripCar);
            }
        }

        return tripMapper.toResponse(savedEntity);
    }

    @Override
    public TripResponse update(UUID id, TripUpdate updateDto) {
        TripDbModel entity = tripRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException(TripDbModel.class, id));

        entity.setDepartureTime(updateDto.getDepartureTime());
        entity.setPrice(updateDto.getPrice());
        entity.setAvailableSeats(updateDto.getAvailableSeats());

        if (updateDto.getRouteId() != null) {
            RouteDbModel route = routeRepository.findById(updateDto.getRouteId())
                    .orElseThrow(() -> new ResourceNotFoundException(RouteDbModel.class, updateDto.getRouteId()));
            entity.setRoute(route);
        }

        TripDbModel updatedEntity = tripRepository.save(entity);

        // Cập nhật danh sách xe nếu có thay đổi
        if (updateDto.getCarIds() != null) {
            // Xóa tất cả TripCar hiện tại của trip này
            List<TripCarDbModel> existingTripCars = tripCarRepository.findByTripIdAndNotDeleted(id);
            for (TripCarDbModel tripCar : existingTripCars) {
                tripCarRepository.softDeleteById(tripCar.getId());
            }

            // Tạo lại TripCar mới từ danh sách carIds
            if (updateDto.getCarIds() != null && !updateDto.getCarIds().isEmpty()) {
                for (UUID carId : updateDto.getCarIds()) {
                    CarDbModel car = carRepository.findById(carId)
                            .orElseThrow(() -> new ResourceNotFoundException(CarDbModel.class, carId));

                    TripCarDbModel tripCar = new TripCarDbModel();
                    tripCar.setTrip(updatedEntity);
                    tripCar.setCar(car);
                    tripCar.setPrice(updateDto.getPrice());
                    tripCar.setAvailableSeats(updateDto.getAvailableSeats());

                    tripCarRepository.save(tripCar);
                }
            }
        }

        return tripMapper.toResponse(updatedEntity);
    }

    @Override
    public void delete(UUID id) {
        tripRepository.softDeleteById(id);
    }

    @Override
    public void activate(UUID id) {
        TripDbModel entity = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(TripDbModel.class, id));
        entity.setIsDeleted(false);
        tripRepository.save(entity);
    }

    @Override
    public void deactivate(UUID id) {
        delete(id);
    }

    @Override
    public List<TripSelectResponse> findAllForSelect() {
        List<TripDbModel> entities = tripRepository.findAllNotDeleted();
        return entities.stream()
                .map(tripMapper::toSelectResponse)
                .toList();
    }

    @Override
    public Page<TripSelectResponse> findAllForSelect(TripQuery query) {
        Specification<TripDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<TripDbModel> entities = tripRepository.findAll(spec, pageable);
        return entities.map(tripMapper::toSelectResponse);
    }

    private Specification<TripDbModel> buildSpecification(TripQuery query) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.or(cb.equal(root.get("isDeleted"), false), cb.isNull(root.get("isDeleted"))));

            if (query.getRouteId() != null) {
                predicates.add(cb.equal(root.get("route")
                        .get("id"), query.getRouteId()));
            }
            if (query.getDepartureTimeFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("departureTime"), query.getDepartureTimeFrom()));
            }
            if (query.getDepartureTimeTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("departureTime"), query.getDepartureTimeTo()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Pageable buildPageable(TripQuery query) {
        Sort.Direction direction = Sort.Direction.fromString(query.getSortDirection());
        Sort sort = Sort.by(direction, query.getSortBy());
        return PageRequest.of(query.getPage(), query.getSize(), sort);
    }
}
