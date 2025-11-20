package org.example.bookvexebej2e.services.notification;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Strings;
import org.example.bookvexebej2e.exceptions.ForbiddenException;
import org.example.bookvexebej2e.exceptions.ResourceNotFoundException;
import org.example.bookvexebej2e.mappers.NotificationMapper;
import org.example.bookvexebej2e.models.db.*;
import org.example.bookvexebej2e.models.dto.notification.*;
import org.example.bookvexebej2e.repositories.booking.BookingRepository;
import org.example.bookvexebej2e.repositories.notification.NotificationRepository;
import org.example.bookvexebej2e.repositories.notification.NotificationTypeRepository;
import org.example.bookvexebej2e.repositories.trip.TripRepository;
import org.example.bookvexebej2e.repositories.user.UserRepository;
import org.example.bookvexebej2e.services.external.MailingService;
import org.example.bookvexebej2e.services.external.WebSocketService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final BookingRepository bookingRepository;
    private final TripRepository tripRepository;
    private final NotificationTypeRepository notificationTypeRepository;
    private final UserRepository userRepository;
    private final MailingService mailingService;
    private final WebSocketService webSocketService;
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
        Specification<NotificationDbModel> spec = buildSpecification(query, null);
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
        NotificationDbModel entity = notificationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(NotificationDbModel.class, id));
        entity.setIsDeleted(true);
        notificationRepository.save(entity);
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
        Specification<NotificationDbModel> spec = buildSpecification(query, null);
        Pageable pageable = buildPageable(query);
        Page<NotificationDbModel> entities = notificationRepository.findAll(spec, pageable);
        return entities.map(notificationMapper::toSelectResponse);
    }

    @Override
    public boolean existsByUserAndBookingAndTripAndType(UUID userId, UUID bookingId, UUID tripId, String typeCode) {
        return notificationRepository.existsByUserIdAndBookingIdAndTripIdAndTypeCode(
            userId, bookingId, tripId, typeCode);
    }

    /**
     * Enhanced high-level service method to send a notification that handles guest users
     */
    @Transactional
    public NotificationResponse sendNotification(UUID userId, String typeCode, String title, String message,
        UUID bookingId, UUID tripId, String channel, Boolean sendEmail, Boolean shouldSave) {

        return sendNotificationInternal(userId, null, typeCode, title, message, bookingId, tripId, channel, sendEmail,
            shouldSave);
    }

    /**
     * Overload method that allows specifying an explicit email address
     */
    @Transactional
    public NotificationResponse sendNotification(UUID userId, String toEmail, String typeCode, String title,
        String message, UUID bookingId, UUID tripId, String channel, Boolean sendEmail, Boolean shouldSave) {

        return sendNotificationInternal(userId, toEmail, typeCode, title, message, bookingId, tripId, channel,
            sendEmail, shouldSave);
    }

    /**
     * NEW: Guest-friendly notification method that doesn't require user ID
     */
    public NotificationResponse sendGuestNotification(String toEmail, String typeCode, String title, String message,
        UUID bookingId, UUID tripId, String channel, Boolean sendEmail, Boolean shouldSave) {

        log.info("Sending guest notification for booking {} with email {}", bookingId, toEmail);

        // For guest notifications, we cannot save to DB or send WebSocket
        if (Boolean.TRUE.equals(shouldSave)) {
            log.warn("Cannot save guest notification without user ID. Skipping database save.");
            shouldSave = false;
        }

        // Create a temporary notification response without user context
        NotificationDbModel entity = createGuestNotification(typeCode, title, message, bookingId, tripId, channel);

        // Handle email if requested
        if (Boolean.TRUE.equals(sendEmail)) {
            try {
                if (toEmail != null && !toEmail.isBlank()) {
                    mailingService.sendEmail(toEmail, title, message);
                    log.info("Email sent to guest: {}", toEmail);
                } else {
                    // Try to resolve email from booking if available
                    String resolvedEmail = resolveGuestEmail(bookingId);
                    if (resolvedEmail != null) {
                        mailingService.sendEmail(resolvedEmail, title, message);
                        log.info("Email sent to guest (resolved from booking): {}", resolvedEmail);
                    } else {
                        log.warn("Email requested for guest notification but no email available for booking {}",
                            bookingId);
                    }
                }
            } catch (Exception e) {
                log.error("Failed to send email for guest notification: {}", e.getMessage(), e);
            }
        }

        // WebSocket is not available for guests (no user ID)
        log.info("WebSocket notification skipped for guest (no user ID)");

        return notificationMapper.toResponse(entity);
    }


    // -------------------------------------------------------------
    // Internal Method with Guest Support
    // -------------------------------------------------------------
    private NotificationResponse sendNotificationInternal(UUID userId, String toEmail, String typeCode, String title,
        String message, UUID bookingId, UUID tripId, String channel, Boolean sendEmail, Boolean shouldSave) {

        // Determine if this is a guest scenario
        boolean isGuest = (userId == null);

        if (isGuest) {
            log.info("Guest notification detected - using guest notification flow");
            return sendGuestNotification(toEmail, typeCode, title, message, bookingId, tripId, channel, sendEmail,
                shouldSave);
        }

        NotificationDbModel entity;

        try {
            // Enhanced email handling for core service
            if (Boolean.TRUE.equals(sendEmail)) {
                String finalRecipientEmail = toEmail;

                // If no explicit email provided, try to resolve it from available sources
                if (finalRecipientEmail == null || finalRecipientEmail.isBlank()) {
                    finalRecipientEmail = resolveRecipientEmail(userId, bookingId);
                }

                if (finalRecipientEmail != null && !finalRecipientEmail.isBlank()) {
                    mailingService.sendEmail(finalRecipientEmail, title, message);
                    log.info("Email sent to: {}", finalRecipientEmail);
                } else {
                    log.warn("Email requested but no recipient email could be resolved for user {} and booking {}",
                        userId, bookingId);
                }
            }

            if (Boolean.TRUE.equals(shouldSave)) {
                entity = saveNotification(userId, typeCode, title, message, bookingId, tripId, channel);
                log.info("Notification saved and sent to user {}. ID: {}", userId, entity.getId());
            } else {
                entity = createUnsavedNotification(userId, typeCode, title, message, bookingId, tripId, channel);
                log.info("DEBUG Notification created (unsaved) and sent to user {}.", userId);
            }

            // WebSocket only for authenticated users
            webSocketService.notifyUser(userId, "NEW_NOTIFICATION");
            return notificationMapper.toResponse(entity);
        } catch (Exception e) {
            log.error("Failed to send notification: {}", e.getMessage(), e);
            // Return a dummy response to avoid throwing exception
            NotificationDbModel dummy = new NotificationDbModel();
            dummy.setId(UUID.randomUUID());
            dummy.setTitle(title);
            dummy.setMessage(message);
            dummy.setChannel(channel);
            dummy.setIsSent(false);
            return notificationMapper.toResponse(dummy);
        }
    }

    /**
     * Resolves recipient email by trying multiple sources in priority order for core service
     */
    private String resolveRecipientEmail(UUID userId, UUID bookingId) {
        // Try to get email from user first
        try {
            UserDbModel user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(UserDbModel.class, userId));

            // Check if user has email directly
            //            if (user.getEmail() != null && !user.getEmail()
            //                .isBlank()) {
            //                return user.getEmail();
            //            }

            // If user doesn't have direct email, check related entities
            if (user.getCustomer() != null && user.getCustomer()
                .getEmail() != null && !user.getCustomer()
                .getEmail()
                .isBlank()) {
                return user.getCustomer()
                    .getEmail();
            }

            if (user.getEmployee() != null && user.getEmployee()
                .getEmail() != null && !user.getEmployee()
                .getEmail()
                .isBlank()) {
                return user.getEmployee()
                    .getEmail();
            }
        } catch (ResourceNotFoundException e) {
            log.warn("User {} not found when trying to resolve email", userId);
        }

        // If user lookup failed but we have bookingId, try to get email from booking
        if (bookingId != null) {
            try {
                BookingDbModel booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new ResourceNotFoundException(BookingDbModel.class, bookingId));

                if (booking.getCustomer() != null && booking.getCustomer()
                    .getEmail() != null && !booking.getCustomer()
                    .getEmail()
                    .isBlank()) {
                    return booking.getCustomer()
                        .getEmail();
                }
            } catch (ResourceNotFoundException e) {
                log.warn("Booking {} not found when trying to resolve email", bookingId);
            }
        }

        log.warn("Could not resolve email for user {} with booking {}", userId, bookingId);
        return null;
    }

    /**
     * NEW: Create guest notification without user context
     */
    private NotificationDbModel createGuestNotification(String typeCode, String title, String message, UUID bookingId
        , UUID tripId, String channel) {

        NotificationDbModel entity = new NotificationDbModel();
        entity.setId(UUID.randomUUID()); // Temporary ID for response
        entity.setChannel(channel != null ? channel : "EMAIL"); // Default channel for guests
        entity.setTitle(title);
        entity.setMessage(message);
        entity.setIsRead(false);
        entity.setIsSent(true);
        entity.setSentAt(LocalDateTime.now());
        entity.setCreatedDate(LocalDateTime.now());
        entity.setUpdatedDate(LocalDateTime.now());

        // Try to set notification type if available
        if (typeCode != null && !typeCode.trim()
            .isEmpty()) {
            try {
                NotificationTypeDbModel type = notificationTypeRepository.findByCode(typeCode)
                    .orElseThrow(() -> new ResourceNotFoundException(NotificationTypeDbModel.class, typeCode));
                entity.setType(type);
            } catch (ResourceNotFoundException e) {
                log.warn("Notification type {} not found for guest notification", typeCode);
            }
        }

        // Set booking/trip references if available
        if (bookingId != null) {
            try {
                BookingDbModel booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new ResourceNotFoundException(BookingDbModel.class, bookingId));
                entity.setBooking(booking);
            } catch (ResourceNotFoundException e) {
                log.warn("Booking {} not found for guest notification", bookingId);
            }
        }

        if (tripId != null) {
            try {
                TripDbModel trip = tripRepository.findById(tripId)
                    .orElseThrow(() -> new ResourceNotFoundException(TripDbModel.class, tripId));
                entity.setTrip(trip);
            } catch (ResourceNotFoundException e) {
                log.warn("Trip {} not found for guest notification", tripId);
            }
        }

        return entity;
    }

    /**
     * NEW: Resolve email for guest notifications
     */
    private String resolveGuestEmail(UUID bookingId) {
        if (bookingId != null) {
            try {
                BookingDbModel booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new ResourceNotFoundException(BookingDbModel.class, bookingId));

                if (booking.getCustomer() != null && booking.getCustomer()
                    .getEmail() != null && !booking.getCustomer()
                    .getEmail()
                    .isBlank()) {
                    return booking.getCustomer()
                        .getEmail();
                }
            } catch (ResourceNotFoundException e) {
                log.warn("Booking {} not found when trying to resolve guest email", bookingId);
            }
        }
        return null;
    }


    // Helper for unsaved response
    private NotificationDbModel createUnsavedNotification(UUID userId, String typeCode, String title, String message,
        UUID bookingId, UUID tripId, String channel) {

        NotificationDbModel entity = new NotificationDbModel();
        entity.setId(UUID.randomUUID()); // Give it a temporary ID
        entity.setChannel(channel);
        entity.setTitle(title);
        entity.setMessage(message);
        entity.setIsRead(false);
        entity.setIsSent(true);
        entity.setSentAt(LocalDateTime.now());
        entity.setCreatedDate(LocalDateTime.now());
        entity.setUpdatedDate(LocalDateTime.now());
        entity.setCreatedBy(userId);
        entity.setUpdatedBy(userId);

        if (typeCode != null && !typeCode.trim()
            .isEmpty()) {
            NotificationTypeDbModel type = notificationTypeRepository.findByCode(typeCode)
                .orElseThrow(() -> new ResourceNotFoundException(NotificationTypeDbModel.class, typeCode));
            entity.setType(type);
        }

        if (bookingId != null) {
            BookingDbModel booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException(BookingDbModel.class, bookingId));
            entity.setBooking(booking);
        }

        if (tripId != null) {
            TripDbModel trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException(TripDbModel.class, tripId));
            entity.setTrip(trip);
        }

        return entity;
    }

    /**
     * Saves the notification entity to the database.
     */
    private NotificationDbModel saveNotification(UUID userId, String typeCode, String title, String message,
        UUID bookingId, UUID tripId, String channel) {

        UserDbModel user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(UserDbModel.class, userId));

        if (Strings.CI.contains(typeCode, "DEPARTURE") && Strings.CI.contains(typeCode, "REMINDER")) {
            Optional<NotificationDbModel> existing = notificationRepository.findExistingReminder(userId, bookingId,
                tripId, typeCode);

            if (existing.isPresent()) {
                log.info("Notification already sent for user={}, booking={}, trip={}", userId, bookingId, tripId);
                return existing.get();
            }
        }

        NotificationDbModel entity = new NotificationDbModel();
        entity.setUser(user);
        entity.setChannel(channel != null ? channel : "APP");
        entity.setTitle(title);
        entity.setMessage(message);
        entity.setIsRead(false);
        entity.setIsSent(true);
        entity.setSentAt(LocalDateTime.now());

        if (typeCode != null && !typeCode.trim()
            .isEmpty()) {
            NotificationTypeDbModel type = notificationTypeRepository.findByCode(typeCode)
                .orElseThrow(() -> new ResourceNotFoundException(NotificationTypeDbModel.class, typeCode));
            entity.setType(type);
        }

        if (bookingId != null) {
            BookingDbModel booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException(BookingDbModel.class, bookingId));
            entity.setBooking(booking);
        }

        if (tripId != null) {
            TripDbModel trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException(TripDbModel.class, tripId));
            entity.setTrip(trip);
        }

        return notificationRepository.save(entity);
    }

    @Override
    public Page<NotificationResponse> getMyNotifications(UUID userId, NotificationQuery query) {
        Specification<NotificationDbModel> spec = buildSpecification(query, false);
        Pageable pageable = buildPageable(query);
        Page<NotificationDbModel> entities = notificationRepository.findAll(spec, pageable);
        return entities.map(notificationMapper::toResponse);
    }

    @Override
    @Transactional
    public void markNotificationAsRead(UUID notificationId, UUID userId) {
        NotificationDbModel entity = notificationRepository.findByIdAndNotDeleted(notificationId)
            .orElseThrow(() -> new ResourceNotFoundException(NotificationDbModel.class, notificationId));

        // Security check: only the owner can mark as read
        if (!entity.getUser()
            .getId()
            .equals(userId)) {
            throw new ForbiddenException("You do not have permission to modify this notification.");
        }

        entity.setIsRead(true);
        notificationRepository.save(entity);

        // Ping frontend to update unread count
        webSocketService.notifyUser(userId, "READ_NOTIFICATION");
    }

    @Override
    @Transactional
    public void deleteNotification(UUID notificationId, UUID userId) {
        NotificationDbModel entity = notificationRepository.findByIdAndNotDeleted(notificationId)
            .orElseThrow(() -> new ResourceNotFoundException(NotificationDbModel.class, notificationId));

        // Security check: only the owner can delete
        if (!entity.getUser()
            .getId()
            .equals(userId)) {
            throw new ForbiddenException("You do not have permission to delete this notification.");
        }

        notificationRepository.softDeleteById(notificationId);

        // Ping frontend to remove notification from list
        webSocketService.notifyUser(userId, "DELETED_NOTIFICATION");
    }

    @Override
    public int countUnreadNotifications(UUID userId) {
        return (int) notificationRepository.count((root, cq, cb) -> cb.and(cb.equal(root.get("user")
            .get("id"), userId), cb.isFalse(root.get("isRead")), cb.isFalse(root.get("isDeleted"))));
    }

    private Specification<NotificationDbModel> buildSpecification(NotificationQuery query, Boolean isDeletedFilter) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (isDeletedFilter != null) {
                // If isDeletedFilter is provided (true or false), apply it directly.
                predicates.add(cb.equal(root.get("isDeleted"), isDeletedFilter));
            }

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
            if (query.getIsRead() != null) {
                predicates.add(cb.equal(root.get("isRead"), query.getIsRead()));
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
