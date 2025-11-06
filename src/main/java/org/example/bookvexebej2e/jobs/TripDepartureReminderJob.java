package org.example.bookvexebej2e.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookvexebej2e.dto.kafka.NotificationKafkaDTO;
import org.example.bookvexebej2e.models.db.BookingDbModel;
import org.example.bookvexebej2e.models.db.TripDbModel;
import org.example.bookvexebej2e.repositories.trip.TripRepository;
import org.example.bookvexebej2e.services.kafka.KafkaProducerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TripDepartureReminderJob {

    private final TripRepository tripRepository;
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
                if (booking.getUser() != null) {
                    String message = String.format("Chuyến đi từ %s đến %s của bạn sắp khởi hành lúc %s.",
                            trip.getRoute().getDeparture(),
                            trip.getRoute().getDestination(),
                            trip.getDepartureTime().toLocalTime());

                    NotificationKafkaDTO notificationDto = new NotificationKafkaDTO(
                            booking.getUser().getId(),
                            message,
                            "TRIP_DEPARTURE_REMINDER",
                            "/my-tickets/" + booking.getId()
                    );

                    kafkaProducerService.sendNotification(notificationDto);
                }
            }
        }

        log.info("Finished sending reminders for upcoming trips.");
    }
}

