package com.bookvexe.notificationservice.kafka;

import com.bookvexe.notificationservice.dto.NotificationKafkaDTO;
import com.bookvexe.notificationservice.entities.NotificationDbModel;
import com.bookvexe.notificationservice.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "notification-topic", groupId = "notification_group")
    public void consume(NotificationKafkaDTO kafkaDTO) {
        log.info("Received notification from Kafka: {}", kafkaDTO);
        try {
            NotificationDbModel notification = new NotificationDbModel();
            notification.setUser(kafkaDTO.getUserId());
            notification.setMessage(kafkaDTO.getMessage());
            notification.setIsRead(false);

            NotificationDbModel savedNotification = notificationRepository.save(notification);

            // Gửi thông báo real-time tới user cụ thể qua WebSocket
            String userDestination = "/topic/notifications/" + savedNotification.getUser();
            messagingTemplate.convertAndSend(userDestination, savedNotification);
            log.info("Sent notification to WebSocket destination: {}", userDestination);

        } catch (Exception e) {
            log.error("Error processing notification from Kafka", e);
        }
    }
}

