package org.example.bookvexebej2e.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookvexebej2e.models.db.BookingDbModel;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.repositories.booking.BookingRepository;
import org.example.bookvexebej2e.repositories.user.UserRepository;
import org.example.bookvexebej2e.services.notification.NotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TripDepartureReminderJob {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    private static final String REMINDER_TYPE_CODE = "TYPE_DEPARTURE_REMINDER";

    @Scheduled(cron = "0 * * * * ?")
    public void remindUsersOfUpcomingTrips() {
        log.info("Running trip departure reminder job...");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reminderWindow = now.plusHours(1);

        List<BookingDbModel> unnotifiedBookings = bookingRepository.findBookingsAwaitingReminder(now, reminderWindow,
            REMINDER_TYPE_CODE);

        if (unnotifiedBookings.isEmpty()) {
            log.info("No unnotified bookings found.");
            return;
        }

        log.info("Found {} bookings requiring a reminder. Sending notifications...", unnotifiedBookings.size());

        for (BookingDbModel booking : unnotifiedBookings) {
            if (booking.getCustomer() == null || booking.getTrip() == null)
                continue;

            // 1. Resolve User (Necessary for WebSocket/Ownership/Email fallbacks)
            UserDbModel user = userRepository.findByCustomerId(booking.getCustomer()
                    .getId())
                .orElse(null);

            if (user == null) {
                log.warn("Skipping reminder: UserDbModel not found for Customer ID {}", booking.getCustomer()
                    .getId());
                continue;
            }

            // 2. Construct Message
            String message = String.format("Chuyến đi từ %s đến %s của bạn sắp khởi hành lúc %s.", booking.getTrip()
                .getRoute()
                .getStartLocation(), booking.getTrip()
                .getRoute()
                .getEndLocation(), booking.getTrip()
                .getDepartureTime()
                .toLocalTime());

            String email = booking.getCustomer()
                .getEmail();

            // 3. Send Notification
            notificationService.sendNotification(user.getId(), email, // Pass email explicitly for Customer
                REMINDER_TYPE_CODE, "Sắp khởi hành: Nhắc nhở chuyến đi", message, booking.getId(), booking.getTrip()
                    .getId(), "CHANNEL_BOOKING", email != null, true // shouldSave
            );
        }

        log.info("Finished sending reminders for unnotified bookings.");
    }
}
