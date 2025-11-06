package org.example.bookvexebej2e.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookvexebej2e.models.db.BookingDbModel;
import org.example.bookvexebej2e.models.db.TripDbModel;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.models.dto.kafka.NotificationKafkaDTO;
import org.example.bookvexebej2e.repositories.trip.TripRepository;
import org.example.bookvexebej2e.repositories.user.UserRepository;
import org.example.bookvexebej2e.services.kafka.KafkaProducerService;
import org.example.bookvexebej2e.services.notification.NotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TripDepartureReminderJob {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final KafkaProducerService kafkaProducerService;

    // Chạy mỗi phút
    @Scheduled(cron = "0 * * * * ?")
    public void remindUsersOfUpcomingTrips() {
        log.info("Running trip departure reminder job...");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reminderWindow = now.plusHours(1); // Gửi lời nhắc cho các chuyến đi trong vòng 1 giờ tới

        List<TripDbModel> upcomingTrips = tripRepository.findByDepartureTimeBetween(now, reminderWindow);

        if (upcomingTrips.isEmpty()) {
            log.info("No upcoming trips to remind.");
            return;
        }

        log.info("Found {} upcoming trips. Sending reminders...", upcomingTrips.size());

        for (TripDbModel trip : upcomingTrips) {
            for (BookingDbModel booking : trip.getBookings()) {
                if (booking.getCustomer() != null) {
                    NotificationKafkaDTO notificationDto = getNotificationKafkaDTO(trip, booking);
                    kafkaProducerService.sendNotification(notificationDto);
                }
            }
        }

        log.info("Finished sending reminders for upcoming trips.");
    }

    private NotificationKafkaDTO getNotificationKafkaDTO(TripDbModel trip, BookingDbModel booking) {
        String message = String.format("Chuyến đi từ %s đến %s của bạn sắp khởi hành lúc %s.", trip.getRoute()
            .getStartLocation(), trip.getRoute()
            .getEndLocation(), trip.getDepartureTime()
            .toLocalTime());

        UserDbModel user = userRepository.findByCustomerId(booking.getCustomer()
                .getId())
            .orElseThrow();

        notificationService.sendNotification(user.getId(), "TYPE_DEPARTURE_REMINDER", message, message, booking.getId(),
            booking.getTrip()
                .getId(), "CHANNEL_BOOKING", user.getCustomer()
                .getEmail() != null, true);

        NotificationKafkaDTO notificationDto = new NotificationKafkaDTO(user.getId(), user.getUsername(),
            booking.getCustomer()
                .getEmail(), booking.getCustomer()
            .getPhone());

        return notificationDto;
    }
}

