package org.example.bookvexebej2e.services.car;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.exceptions.ResourceNotFoundException;
import org.example.bookvexebej2e.mappers.CarSeatMapper;
import org.example.bookvexebej2e.models.db.CarDbModel;
import org.example.bookvexebej2e.models.db.CarSeatDbModel;
import org.example.bookvexebej2e.models.dto.car.CarSeatCreate;
import org.example.bookvexebej2e.models.dto.car.CarSeatQuery;
import org.example.bookvexebej2e.models.dto.car.CarSeatResponse;
import org.example.bookvexebej2e.models.dto.car.CarSeatSelectResponse;
import org.example.bookvexebej2e.models.dto.car.CarSeatUpdate;
import org.example.bookvexebej2e.repositories.car.CarRepository;
import org.example.bookvexebej2e.repositories.car.CarSeatRepository;
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
public class CarSeatServiceImpl implements CarSeatService {

    private final CarSeatRepository carSeatRepository;
    private final CarRepository carRepository;
    private final CarSeatMapper carSeatMapper;

    @Override
    public List<CarSeatResponse> findAll() {
        List<CarSeatDbModel> entities = carSeatRepository.findAllNotDeleted();
        return entities.stream()
                .map(carSeatMapper::toResponse)
                .toList();
    }

    @Override
    public Page<CarSeatResponse> findAll(CarSeatQuery query) {
        Specification<CarSeatDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<CarSeatDbModel> entities = carSeatRepository.findAll(spec, pageable);
        return entities.map(carSeatMapper::toResponse);
    }

    @Override
    public CarSeatResponse findById(UUID id) {
        CarSeatDbModel entity = carSeatRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException(CarSeatDbModel.class, id));
        return carSeatMapper.toResponse(entity);
    }

    @Override
    public CarSeatResponse create(CarSeatCreate createDto) {
        CarSeatDbModel entity = new CarSeatDbModel();
        entity.setSeatNumber(createDto.getSeatNumber());
        entity.setSeatPosition(createDto.getSeatPosition());

        CarDbModel car = carRepository.findById(createDto.getCarId())
                .orElseThrow(() -> new ResourceNotFoundException(CarDbModel.class, createDto.getCarId()));
        entity.setCar(car);

        CarSeatDbModel savedEntity = carSeatRepository.save(entity);
        return carSeatMapper.toResponse(savedEntity);
    }

    @Override
    public CarSeatResponse update(UUID id, CarSeatUpdate updateDto) {
        CarSeatDbModel entity = carSeatRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException(CarSeatDbModel.class, id));

        entity.setSeatNumber(updateDto.getSeatNumber());
        entity.setSeatPosition(updateDto.getSeatPosition());

        if (updateDto.getCarId() != null) {
            CarDbModel car = carRepository.findById(updateDto.getCarId())
                    .orElseThrow(() -> new ResourceNotFoundException(CarDbModel.class, updateDto.getCarId()));
            entity.setCar(car);
        }

        CarSeatDbModel updatedEntity = carSeatRepository.save(entity);
        return carSeatMapper.toResponse(updatedEntity);
    }

    @Override
    public void delete(UUID id) {
        carSeatRepository.softDeleteById(id);
    }

    @Override
    public void activate(UUID id) {
        CarSeatDbModel entity = carSeatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CarSeatDbModel.class, id));
        entity.setIsDeleted(false);
        carSeatRepository.save(entity);
    }

    @Override
    public void deactivate(UUID id) {
        CarSeatDbModel entity = carSeatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CarSeatDbModel.class, id));
        entity.setIsDeleted(true);
        carSeatRepository.save(entity);
    }

    @Override
    public List<CarSeatSelectResponse> findAllForSelect() {
        List<CarSeatDbModel> entities = carSeatRepository.findAllNotDeleted();
        return entities.stream()
                .map(carSeatMapper::toSelectResponse)
                .toList();
    }

    @Override
    public Page<CarSeatSelectResponse> findAllForSelect(CarSeatQuery query) {
        Specification<CarSeatDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<CarSeatDbModel> entities = carSeatRepository.findAll(spec, pageable);
        return entities.map(carSeatMapper::toSelectResponse);
    }

    private Specification<CarSeatDbModel> buildSpecification(CarSeatQuery query) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Remove the isDeleted filter to show all records including deleted ones

            if (query.getCarId() != null) {
                predicates.add(cb.equal(root.get("car")
                        .get("id"), query.getCarId()));
            }
            if (query.getSeatNumber() != null && !query.getSeatNumber()
                    .isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("seatNumber")), "%" + query.getSeatNumber()
                        .toLowerCase() + "%"));
            }
            if (query.getSeatPosition() != null && !query.getSeatPosition()
                    .isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("seatPosition")), "%" + query.getSeatPosition()
                        .toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Pageable buildPageable(CarSeatQuery query) {
        Sort.Direction direction = Sort.Direction.fromString(query.getSortDirection());
        Sort sort = Sort.by(direction, query.getSortBy());
        return PageRequest.of(query.getPage(), query.getSize(), sort);
    }

    @Override
    public void createSeatsForCar(UUID carId, Integer seatCount) {
        if (carId == null || seatCount == null || seatCount <= 0) {
            return;
        }

        CarDbModel car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException(CarDbModel.class, carId));

        // First, check if there are existing seats and delete them permanently
        List<CarSeatDbModel> existingSeats = carSeatRepository.findByCarId(carId);
        if (!existingSeats.isEmpty()) {
            carSeatRepository.deleteAll(existingSeats);
        }

        List<CarSeatDbModel> seats = new ArrayList<>();

        // Create seats with numbering pattern (A1, A2, A3, B1, B2, B3, etc.)
        for (int i = 1; i <= seatCount; i++) {
            CarSeatDbModel seat = new CarSeatDbModel();
            seat.setCar(car);

            // Generate seat number (A1, A2, A3, B1, B2, B3, etc.) - 3 seats per row
            char rowLetter = (char) ('A' + (i - 1) / 3); // 3 seats per row
            int seatInRow = ((i - 1) % 3) + 1;
            String seatNumber = rowLetter + String.valueOf(seatInRow);

            seat.setSeatNumber(seatNumber);

            // Generate seat position description
            String position = "Hàng " + rowLetter + " - Ghế " + seatInRow;
            if (seatInRow == 1 || seatInRow == 3) {
                position += " (Cạnh cửa sổ)";
            } else {
                position += " (Giữa)";
            }
            seat.setSeatPosition(position);

            seats.add(seat);
        }

        carSeatRepository.saveAll(seats);
    }

    @Override
    public void deleteAllSeatsByCar(UUID carId) {
        if (carId == null) {
            return;
        }

        List<CarSeatDbModel> existingSeats = carSeatRepository.findByCarId(carId);
        if (!existingSeats.isEmpty()) {
            carSeatRepository.deleteAll(existingSeats);
        }
    }
}
