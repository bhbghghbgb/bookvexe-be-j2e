package org.example.bookvexebej2e.service.trip;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.mappers.TripCarMapper;
import org.example.bookvexebej2e.models.db.CarDbModel;
import org.example.bookvexebej2e.models.db.TripCarDbModel;
import org.example.bookvexebej2e.models.db.TripDbModel;
import org.example.bookvexebej2e.models.dto.trip.*;
import org.example.bookvexebej2e.repository.car.CarRepository;
import org.example.bookvexebej2e.repository.trip.TripCarRepository;
import org.example.bookvexebej2e.repository.trip.TripRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TripCarServiceImpl implements TripCarService {

    private final TripCarRepository tripCarRepository;
    private final TripRepository tripRepository;
    private final CarRepository carRepository;
    private final TripCarMapper tripCarMapper;

    @Override
    public List<TripCarResponse> findAll() {
        List<TripCarDbModel> entities = tripCarRepository.findAllByIsDeletedFalse();
        return entities.stream()
            .map(tripCarMapper::toResponse)
            .toList();
    }

    @Override
    public Page<TripCarResponse> findAll(TripCarQuery query) {
        Specification<TripCarDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<TripCarDbModel> entities = tripCarRepository.findAll(spec, pageable);
        return entities.map(tripCarMapper::toResponse);
    }

    @Override
    public TripCarResponse findById(UUID id) {
        TripCarDbModel entity = tripCarRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new RuntimeException("TripCar not found with id: " + id));
        return tripCarMapper.toResponse(entity);
    }

    @Override
    public TripCarResponse create(TripCarCreate createDto) {
        TripCarDbModel entity = new TripCarDbModel();
        entity.setPrice(createDto.getPrice());
        entity.setAvailableSeats(createDto.getAvailableSeats());

        TripDbModel trip = tripRepository.findById(createDto.getTripId())
            .orElseThrow(() -> new RuntimeException("Trip not found with id: " + createDto.getTripId()));
        entity.setTrip(trip);

        CarDbModel car = carRepository.findById(createDto.getCarId())
            .orElseThrow(() -> new RuntimeException("Car not found with id: " + createDto.getCarId()));
        entity.setCar(car);

        TripCarDbModel savedEntity = tripCarRepository.save(entity);
        return tripCarMapper.toResponse(savedEntity);
    }

    @Override
    public TripCarResponse update(UUID id, TripCarUpdate updateDto) {
        TripCarDbModel entity = tripCarRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new RuntimeException("TripCar not found with id: " + id));

        entity.setPrice(updateDto.getPrice());
        entity.setAvailableSeats(updateDto.getAvailableSeats());

        if (updateDto.getTripId() != null) {
            TripDbModel trip = tripRepository.findById(updateDto.getTripId())
                .orElseThrow(() -> new RuntimeException("Trip not found with id: " + updateDto.getTripId()));
            entity.setTrip(trip);
        }

        if (updateDto.getCarId() != null) {
            CarDbModel car = carRepository.findById(updateDto.getCarId())
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + updateDto.getCarId()));
            entity.setCar(car);
        }

        TripCarDbModel updatedEntity = tripCarRepository.save(entity);
        return tripCarMapper.toResponse(updatedEntity);
    }

    @Override
    public void delete(UUID id) {
        tripCarRepository.softDeleteById(id);
    }

    @Override
    public void activate(UUID id) {
        TripCarDbModel entity = tripCarRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("TripCar not found with id: " + id));
        entity.setIsDeleted(false);
        tripCarRepository.save(entity);
    }

    @Override
    public void deactivate(UUID id) {
        delete(id);
    }

    @Override
    public List<TripCarSelectResponse> findAllForSelect() {
        List<TripCarDbModel> entities = tripCarRepository.findAllByIsDeletedFalse();
        return entities.stream()
            .map(tripCarMapper::toSelectResponse)
            .toList();
    }

    private Specification<TripCarDbModel> buildSpecification(TripCarQuery query) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("isDeleted"), false));

            if (query.getTripId() != null) {
                predicates.add(cb.equal(root.get("trip")
                    .get("id"), query.getTripId()));
            }
            if (query.getCarId() != null) {
                predicates.add(cb.equal(root.get("car")
                    .get("id"), query.getCarId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Pageable buildPageable(TripCarQuery query) {
        Sort.Direction direction = Sort.Direction.fromString(query.getSortDirection());
        Sort sort = Sort.by(direction, query.getSortBy());
        return PageRequest.of(query.getPage(), query.getSize(), sort);
    }
}
