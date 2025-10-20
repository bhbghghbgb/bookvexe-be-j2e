package org.example.bookvexebej2e.services.notification;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.exceptions.ResourceNotFoundException;
import org.example.bookvexebej2e.mappers.NotificationMapper;
import org.example.bookvexebej2e.models.db.*;
import org.example.bookvexebej2e.models.dto.notification.*;
import org.example.bookvexebej2e.repositories.booking.BookingRepository;
import org.example.bookvexebej2e.repositories.notification.NotificationRepository;
import org.example.bookvexebej2e.repositories.notification.NotificationTypeRepository;
import org.example.bookvexebej2e.repositories.trip.TripRepository;
import org.example.bookvexebej2e.repositories.user.UserRepository;
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
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final BookingRepository bookingRepository;
    private final TripRepository tripRepository;
    private final NotificationTypeRepository notificationTypeRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public List<NotificationResponse> findAll() {
        List<NotificationDbModel> entities = notificationRepository.findAllNotDeleted();
        return entities.stream()
            .map(notificationMapper::toResponse)
            .toList();
    }

    @Override
    public Page<NotificationResponse> findAll(NotificationQuery query) {
        Specification<NotificationDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<NotificationDbModel> entities = notificationRepository.findAll(spec, pageable);
        return entities.map(notificationMapper::toResponse);
    }

    @Override
    public NotificationResponse findById(UUID id) {
        NotificationDbModel entity = notificationRepository.findByIdAndNotDeleted(id)
            .orElseThrow(() -> new ResourceNotFoundException(NotificationDbModel.class, id));
        return notificationMapper.toResponse(entity);
    }

    @Override
    public NotificationResponse create(NotificationCreate createDto) {
        NotificationDbModel entity = new NotificationDbModel();
        entity.setChannel(createDto.getChannel());
        entity.setTitle(createDto.getTitle());
        entity.setMessage(createDto.getMessage());
        entity.setIsSent(createDto.getIsSent());
        entity.setSentAt(createDto.getSentAt());

        UserDbModel user = userRepository.findById(createDto.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException(UserDbModel.class, createDto.getUserId()));
        entity.setUser(user);

        if (createDto.getBookingId() != null) {
            BookingDbModel booking = bookingRepository.findById(createDto.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException(BookingDbModel.class, createDto.getBookingId()));
            entity.setBooking(booking);
        }

        if (createDto.getTripId() != null) {
            TripDbModel trip = tripRepository.findById(createDto.getTripId())
                .orElseThrow(() -> new ResourceNotFoundException(TripDbModel.class, createDto.getTripId()));
            entity.setTrip(trip);
        }

        NotificationTypeDbModel type = notificationTypeRepository.findById(createDto.getTypeId())
            .orElseThrow(() -> new ResourceNotFoundException(NotificationTypeDbModel.class, createDto.getTypeId()));
        entity.setType(type);

        NotificationDbModel savedEntity = notificationRepository.save(entity);
        return notificationMapper.toResponse(savedEntity);
    }

    @Override
    public NotificationResponse update(UUID id, NotificationUpdate updateDto) {
        NotificationDbModel entity = notificationRepository.findByIdAndNotDeleted(id)
            .orElseThrow(() -> new ResourceNotFoundException(NotificationDbModel.class, id));

        entity.setChannel(updateDto.getChannel());
        entity.setTitle(updateDto.getTitle());
        entity.setMessage(updateDto.getMessage());
        entity.setIsSent(updateDto.getIsSent());
        entity.setSentAt(updateDto.getSentAt());

        if (updateDto.getUserId() != null) {
            UserDbModel user = userRepository.findById(updateDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(UserDbModel.class, updateDto.getUserId()));
            entity.setUser(user);
        }

        if (updateDto.getBookingId() != null) {
            BookingDbModel booking = bookingRepository.findById(updateDto.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException(BookingDbModel.class, updateDto.getBookingId()));
            entity.setBooking(booking);
        }

        if (updateDto.getTripId() != null) {
            TripDbModel trip = tripRepository.findById(updateDto.getTripId())
                .orElseThrow(() -> new ResourceNotFoundException(TripDbModel.class, updateDto.getTripId()));
            entity.setTrip(trip);
        }

        if (updateDto.getTypeId() != null) {
            NotificationTypeDbModel type = notificationTypeRepository.findById(updateDto.getTypeId())
                .orElseThrow(() -> new ResourceNotFoundException(NotificationTypeDbModel.class, updateDto.getTypeId()));
            entity.setType(type);
        }

        NotificationDbModel updatedEntity = notificationRepository.save(entity);
        return notificationMapper.toResponse(updatedEntity);
    }

    @Override
    public void delete(UUID id) {
        notificationRepository.softDeleteById(id);
    }

    @Override
    public void activate(UUID id) {
        NotificationDbModel entity = notificationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(NotificationDbModel.class, id));
        entity.setIsDeleted(false);
        notificationRepository.save(entity);
    }

    @Override
    public void deactivate(UUID id) {
        delete(id);
    }

    @Override
    public List<NotificationSelectResponse> findAllForSelect() {
        List<NotificationDbModel> entities = notificationRepository.findAllNotDeleted();
        return entities.stream()
            .map(notificationMapper::toSelectResponse)
            .toList();
    }

    @Override
    public Page<NotificationSelectResponse> findAllForSelect(NotificationQuery query) {
        Specification<NotificationDbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<NotificationDbModel> entities = notificationRepository.findAll(spec, pageable);
        return entities.map(notificationMapper::toSelectResponse);
    }


    private Specification<NotificationDbModel> buildSpecification(NotificationQuery query) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.or(cb.equal(root.get("isDeleted"), false), cb.isNull(root.get("isDeleted"))));

            if (query.getUserId() != null) {
                predicates.add(cb.equal(root.get("user")
                    .get("id"), query.getUserId()));
            }
            if (query.getBookingId() != null) {
                predicates.add(cb.equal(root.get("booking")
                    .get("id"), query.getBookingId()));
            }
            if (query.getTripId() != null) {
                predicates.add(cb.equal(root.get("trip")
                    .get("id"), query.getTripId()));
            }
            if (query.getTypeId() != null) {
                predicates.add(cb.equal(root.get("type")
                    .get("id"), query.getTypeId()));
            }
            if (query.getChannel() != null && !query.getChannel()
                .isEmpty()) {
                predicates.add(cb.equal(root.get("channel"), query.getChannel()));
            }
            if (query.getIsSent() != null) {
                predicates.add(cb.equal(root.get("isSent"), query.getIsSent()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Pageable buildPageable(NotificationQuery query) {
        Sort.Direction direction = Sort.Direction.fromString(query.getSortDirection());
        Sort sort = Sort.by(direction, query.getSortBy());
        return PageRequest.of(query.getPage(), query.getSize(), sort);
    }
}
