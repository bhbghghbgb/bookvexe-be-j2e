package org.example.bookvexebej2e.services.trip;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
import org.example.bookvexebej2e.services.knowledge.KnowledgeSyncService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final RouteRepository routeRepository;
    private final TripMapper tripMapper;
    private final TripCarRepository tripCarRepository;
    private final CarRepository carRepository;
    private final KnowledgeSyncService knowledgeSyncService;

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
    @Transactional
    public TripResponse create(TripCreate createDto) {
        TripDbModel entity = new TripDbModel();
        entity.setDepartureTime(createDto.getDepartureTime());
        entity.setPrice(createDto.getPrice());
        entity.setAvailableSeats(createDto.getAvailableSeats());

        RouteDbModel route = routeRepository.findById(createDto.getRouteId())
                .orElseThrow(() -> new ResourceNotFoundException(RouteDbModel.class, createDto.getRouteId()));
        entity.setRoute(route);

        TripDbModel savedEntity = tripRepository.save(entity);

        // Tạo TripCar từ danh sách carIds (loại bỏ duplicate)
        if (createDto.getCarIds() != null && !createDto.getCarIds().isEmpty()) {
            List<UUID> uniqueCarIds = createDto.getCarIds().stream()
                    .distinct()
                    .collect(Collectors.toList());

            log.info("Creating trip with {} unique cars", uniqueCarIds.size());
            createTripCars(savedEntity, uniqueCarIds, createDto.getPrice(), createDto.getAvailableSeats());
        }
        
        // Sync knowledge to chat service
        syncTripKnowledge(savedEntity, "CREATE");

        return tripMapper.toResponse(savedEntity);
    }

    @Override
    @Transactional
    public TripResponse update(UUID id, TripUpdate updateDto) {
        TripDbModel entity = tripRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException(TripDbModel.class, id));

        log.info("Updating trip {} with data: {}", id, updateDto);

        // Cập nhật thông tin trip
        entity.setDepartureTime(updateDto.getDepartureTime());
        entity.setPrice(updateDto.getPrice());
        entity.setAvailableSeats(updateDto.getAvailableSeats());

        if (updateDto.getRouteId() != null) {
            RouteDbModel route = routeRepository.findById(updateDto.getRouteId())
                    .orElseThrow(() -> new ResourceNotFoundException(RouteDbModel.class, updateDto.getRouteId()));
            entity.setRoute(route);
        }

        TripDbModel updatedEntity = tripRepository.save(entity);

        // XỬ LÝ CẬP NHẬT TRIPCAR - XÓA CỨNG VÀ TẠO MỚI
        if (updateDto.getCarIds() != null) {
            // Loại bỏ duplicate từ request
            List<UUID> uniqueCarIds = updateDto.getCarIds().stream()
                    .distinct()
                    .collect(Collectors.toList());

            log.info("Updating trip cars: received {} car IDs, {} unique",
                    updateDto.getCarIds().size(), uniqueCarIds.size());

            // Bước 1: Lấy danh sách TripCar hiện tại (bao gồm cả đã xóa mềm)
            List<TripCarDbModel> existingTripCars = tripCarRepository.findByTripIdAndNotDeleted(id);
            log.info("Found {} existing trip cars to delete", existingTripCars.size());

            // Bước 2: XÓA CỨNG TẤT CẢ TripCar cũ khỏi database
            if (!existingTripCars.isEmpty()) {
                tripCarRepository.deleteAll(existingTripCars);
                tripCarRepository.flush(); // Đảm bảo xóa ngay lập tức
                log.info("Hard deleted {} trip cars from database", existingTripCars.size());
            }

            // Bước 3: TẠO MỚI TripCar từ danh sách carIds unique
            if (!uniqueCarIds.isEmpty()) {
                log.info("Creating {} new trip cars", uniqueCarIds.size());
                createTripCars(updatedEntity, uniqueCarIds,
                        updateDto.getPrice(), updateDto.getAvailableSeats());
            } else {
                log.info("No cars to add for this trip");
            }
        }

        // Refresh entity để lấy dữ liệu mới nhất
        tripRepository.flush();
        TripDbModel refreshedEntity = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(TripDbModel.class, id));
        
        // Sync knowledge to chat service
        syncTripKnowledge(refreshedEntity, "UPDATE");

        return tripMapper.toResponse(refreshedEntity);
    }

    @Override
    public void delete(UUID id) {
        // Sync delete to chat service first
        knowledgeSyncService.syncTrip(id.toString(), "DELETE", "", "");
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
        TripDbModel entity = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(TripDbModel.class, id));
        entity.setIsDeleted(true);
        tripRepository.save(entity);
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

    // Private helper methods

    /**
     * Tạo TripCar mới cho một Trip (carIds đã được loại bỏ duplicate)
     */
    private void createTripCars(TripDbModel trip, List<UUID> carIds,
                                java.math.BigDecimal price, Integer availableSeats) {
        for (UUID carId : carIds) {
            CarDbModel car = carRepository.findById(carId)
                    .orElseThrow(() -> new ResourceNotFoundException(CarDbModel.class, carId));

            TripCarDbModel tripCar = new TripCarDbModel();
            tripCar.setTrip(trip);
            tripCar.setCar(car);
            tripCar.setPrice(price);
            tripCar.setAvailableSeats(availableSeats);

            tripCarRepository.save(tripCar);
            log.info("Created trip car: tripId={}, carId={}", trip.getId(), carId);
        }
    }

    private Specification<TripDbModel> buildSpecification(TripQuery query) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (query.getIsDeleted() != null) {
                predicates.add(cb.equal(root.get("isDeleted"), query.getIsDeleted()));
            }

            if (query.getRouteId() != null) {
                predicates.add(cb.equal(root.get("route").get("id"), query.getRouteId()));
            }

            // Support both single departureTime and date range
            if (query.getDepartureTimeFrom() != null && query.getDepartureTimeTo() != null) {
                // Date range filter
                predicates.add(cb.between(root.get("departureTime"), 
                    query.getDepartureTimeFrom(), query.getDepartureTimeTo()));
            } else if (query.getDepartureTimeFrom() != null) {
                // From date only
                predicates.add(cb.greaterThanOrEqualTo(root.get("departureTime"), 
                    query.getDepartureTimeFrom()));
            } else if (query.getDepartureTimeTo() != null) {
                // To date only
                predicates.add(cb.lessThanOrEqualTo(root.get("departureTime"), 
                    query.getDepartureTimeTo()));
            } else if (query.getDepartureTime() != null) {
                // Backward compatibility: single departureTime means >= that time
                predicates.add(cb.greaterThanOrEqualTo(root.get("departureTime"), 
                    query.getDepartureTime()));
            }

            if (query.getPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), query.getPrice()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Pageable buildPageable(TripQuery query) {
        Sort.Direction direction = Sort.Direction.fromString(query.getSortDirection());
        Sort sort = Sort.by(direction, query.getSortBy());
        return PageRequest.of(query.getPage(), query.getSize(), sort);
    }
    
    /**
     * Sync trip knowledge to chat service
     */
    private void syncTripKnowledge(TripDbModel trip, String operation) {
        try {
            String title = String.format("Chuy?n xe %s - %s", 
                    trip.getRoute().getStartLocation(), 
                    trip.getRoute().getEndLocation());
            
            String content = String.format(
                    "Chuy?n xe t? %s d?n %s. " +
                    "Kh?i h�nh l�c %s. " +
                    "Gi� v�: %s VN�. " +
                    "S? gh? tr?ng: %d. " +
                    "Qu�ng du?ng: %.2f km. " +
                    "Th?i gian di chuy?n: kho?ng %d gi?.",
                    trip.getRoute().getStartLocation(),
                    trip.getRoute().getEndLocation(),
                    trip.getDepartureTime().toString(),
                    trip.getPrice().toString(),
                    trip.getAvailableSeats(),
                    trip.getRoute().getDistanceKm(),
                    trip.getRoute().getDistanceKm()
            );
            
            knowledgeSyncService.syncTrip(
                    trip.getId().toString(), 
                    operation, 
                    title, 
                    content
            );
        } catch (Exception e) {
            log.error("Failed to sync trip knowledge: {}", e.getMessage(), e);
            // Kh�ng throw exception d? kh�ng ?nh hu?ng main flow
        }
    }
}

