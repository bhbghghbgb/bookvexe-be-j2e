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

    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "notification-topic", groupId = "notification_group")
    public void consume(NotificationKafkaDTO kafkaDTO) {
        log.info("Received notification from Kafka: {}", kafkaDTO);

        try {
            // Send to userId-based destination
            String userIdDest = "/topic/notifications/" + kafkaDTO.getUserId();
            messagingTemplate.convertAndSend(userIdDest, kafkaDTO);
            log.info("Sent to WebSocket: {}", userIdDest);

            // Optionally send to other identifiers
            if (kafkaDTO.getUsername() != null) {
                String usernameDest = "/topic/notifications/" + kafkaDTO.getUsername();
                messagingTemplate.convertAndSend(usernameDest, kafkaDTO);
                log.info("Sent to WebSocket: {}", usernameDest);
            }

            if (kafkaDTO.getEmail() != null) {
                String emailDest = "/topic/notifications/" + kafkaDTO.getEmail();
                messagingTemplate.convertAndSend(emailDest, kafkaDTO);
                log.info("Sent to WebSocket: {}", emailDest);
            }

            if (kafkaDTO.getPhone() != null) {
                String phoneDest = "/topic/notifications/" + kafkaDTO.getPhone();
                messagingTemplate.convertAndSend(phoneDest, kafkaDTO);
                log.info("Sent to WebSocket: {}", phoneDest);
            }

        } catch (Exception e) {
            log.error("Error processing notification from Kafka", e);
        }
    }
}

