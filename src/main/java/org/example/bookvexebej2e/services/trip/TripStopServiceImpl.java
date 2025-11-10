package org.example.bookvexebej2e.services.trip;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.example.bookvexebej2e.exceptions.ResourceNotFoundException;
import org.example.bookvexebej2e.mappers.TripStopMapper;
import org.example.bookvexebej2e.models.db.TripDbModel;
import org.example.bookvexebej2e.models.db.TripStopDbModel;
import org.example.bookvexebej2e.models.dto.trip.TripStopCreate;
import org.example.bookvexebej2e.models.dto.trip.TripStopQuery;
import org.example.bookvexebej2e.models.dto.trip.TripStopResponse;
import org.example.bookvexebej2e.models.dto.trip.TripStopSelectResponse;
import org.example.bookvexebej2e.models.dto.trip.TripStopUpdate;
import org.example.bookvexebej2e.repositories.trip.TripRepository;
import org.example.bookvexebej2e.repositories.trip.TripStopRepository;
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
public class TripStopServiceImpl implements TripStopService {

    private final TripStopRepository tripStopRepository;
    private final TripRepository tripRepository;
    private final TripStopMapper tripStopMapper;

    @Override
    public List<TripStopResponse> findAll() {
        List<TripStopDbModel> entities = tripStopRepository.findAllNotDeleted();
        return entities.stream()
                .map(tripStopMapper::toResponse)
                .toList();
    }

    @Override
    public Page<TripStopResponse> findAll(TripStopQuery query) {
        Specification<TripStopDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<TripStopDbModel> entities = tripStopRepository.findAll(spec, pageable);
        return entities.map(tripStopMapper::toResponse);
    }

    @Override
    public TripStopResponse findById(UUID id) {
        TripStopDbModel entity = tripStopRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException(TripStopDbModel.class, id));
        return tripStopMapper.toResponse(entity);
    }

    @Override
    public TripStopResponse create(TripStopCreate createDto) {
        TripStopDbModel entity = new TripStopDbModel();
        entity.setStopType(createDto.getStopType());
        entity.setLocation(createDto.getLocation());
        entity.setOrderIndex(createDto.getOrderIndex());

        TripDbModel trip = tripRepository.findById(createDto.getTripId())
                .orElseThrow(() -> new ResourceNotFoundException(TripDbModel.class, createDto.getTripId()));
        entity.setTrip(trip);

        TripStopDbModel savedEntity = tripStopRepository.save(entity);
        return tripStopMapper.toResponse(savedEntity);
    }

    @Override
    public TripStopResponse update(UUID id, TripStopUpdate updateDto) {
        TripStopDbModel entity = tripStopRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException(TripStopDbModel.class, id));

        entity.setStopType(updateDto.getStopType());
        entity.setLocation(updateDto.getLocation());
        entity.setOrderIndex(updateDto.getOrderIndex());

        if (updateDto.getTripId() != null) {
            TripDbModel trip = tripRepository.findById(updateDto.getTripId())
                    .orElseThrow(() -> new ResourceNotFoundException(TripDbModel.class, updateDto.getTripId()));
            entity.setTrip(trip);
        }

        TripStopDbModel updatedEntity = tripStopRepository.save(entity);
        return tripStopMapper.toResponse(updatedEntity);
    }

    @Override
    public void hardDelete(UUID id) {
        TripStopDbModel entity = tripStopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(TripStopDbModel.class, id));

        // Kiểm tra xem có booking nào đang sử dụng TripStop này không
        if (entity.getPickupBookings() != null && !entity.getPickupBookings().isEmpty()) {
            throw new IllegalStateException("Không thể xóa TripStop này vì có " +
                    entity.getPickupBookings().size() + " booking đang sử dụng làm điểm đón");
        }

        if (entity.getDropoffBookings() != null && !entity.getDropoffBookings().isEmpty()) {
            throw new IllegalStateException("Không thể xóa TripStop này vì có " +
                    entity.getDropoffBookings().size() + " booking đang sử dụng làm điểm trả");
        }

        // Nếu không có ràng buộc, thực hiện xóa cứng khỏi database
        tripStopRepository.delete(entity);
    }

    @Override
    public void activate(UUID id) {
        TripStopDbModel entity = tripStopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(TripStopDbModel.class, id));
        entity.setIsDeleted(false);
        tripStopRepository.save(entity);
    }

    @Override
    public void deactivate(UUID id) {
        TripStopDbModel entity = tripStopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(TripStopDbModel.class, id));
        entity.setIsDeleted(true);
        tripStopRepository.save(entity);
    }

    @Override
    public List<TripStopSelectResponse> findAllForSelect() {
        List<TripStopDbModel> entities = tripStopRepository.findAllNotDeleted();
        return entities.stream()
                .map(tripStopMapper::toSelectResponse)
                .toList();
    }

    @Override
    public Page<TripStopSelectResponse> findAllForSelect(TripStopQuery query) {
        Specification<TripStopDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<TripStopDbModel> entities = tripStopRepository.findAll(spec, pageable);
        return entities.map(tripStopMapper::toSelectResponse);
    }

    @Override
    public List<TripStopSelectResponse> findAllForSelectByTrip(UUID tripId) {
        List<TripStopDbModel> entities = tripStopRepository.findAll(
                (root, cq, cb) -> cb.and(
                        cb.or(cb.equal(root.get("isDeleted"), false), cb.isNull(root.get("isDeleted"))),
                        cb.equal(root.get("trip")
                                .get("id"), tripId)));

        return entities.stream()
                .map(tripStopMapper::toSelectResponse)
                .toList();
    }

    private Specification<TripStopDbModel> buildSpecification(TripStopQuery query) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Remove the isDeleted filter to show all records including deleted ones

            if (query.getTripId() != null) {
                predicates.add(cb.equal(root.get("trip")
                        .get("id"), query.getTripId()));
            }
            if (query.getStopType() != null && !query.getStopType()
                    .isEmpty()) {
                predicates.add(cb.equal(root.get("stopType"), query.getStopType()));
            }
            if (query.getLocation() != null && !query.getLocation()
                    .isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("location")), "%" + query.getLocation()
                        .toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Pageable buildPageable(TripStopQuery query) {
        Sort.Direction direction = Sort.Direction.fromString(query.getSortDirection());
        Sort sort = Sort.by(direction, query.getSortBy());
        return PageRequest.of(query.getPage(), query.getSize(), sort);
    }
}
