package org.example.bookvexebej2e.services.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.bookvexebej2e.configs.KafkaTopicConfig;
import org.example.bookvexebej2e.models.dto.kafka.MailKafkaDTO;
import org.example.bookvexebej2e.models.dto.kafka.NotificationKafkaDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendNotification(NotificationKafkaDTO notificationDto) {
        log.info("Attempting to send notification to Kafka: {}", notificationDto);

        // This is non-blocking. It returns a CompletableFuture immediately.
        CompletableFuture<Void> future = kafkaTemplate.send(KafkaTopicConfig.NOTIFICATION_TOPIC, notificationDto)
            .thenAccept(result -> {
                // This block executes when the send is successful (optional logging)
                log.debug("Notification sent successfully to topic: {}, partition: {}, offset: {}",
                    result.getRecordMetadata().topic(),
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset());
            })
            .exceptionally(ex -> {
                // This block executes if the send fails after all retries
                log.error("Final failure: Error sending notification to Kafka after retries. Payload: {}", notificationDto, ex);
                return null;
            });
        // We do not call .get() on 'future' so the calling thread remains non-blocking.
    }

    public void sendMail(MailKafkaDTO mailDto) {
        log.info("Attempting to send mail request to Kafka: {}", mailDto);

        // This is non-blocking. It returns a CompletableFuture immediately.
        CompletableFuture<Void> future = kafkaTemplate.send(KafkaTopicConfig.MAIL_TOPIC, mailDto)
            .thenAccept(result -> {
                // This block executes when the send is successful (optional logging)
                log.debug("Mail request sent successfully to topic: {}, partition: {}, offset: {}",
                    result.getRecordMetadata().topic(),
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset());
            })
            .exceptionally(ex -> {
                // This block executes if the send fails after all retries
                log.error("Final failure: Error sending mail request to Kafka after retries. Payload: {}", mailDto, ex);
                return null;
            });
        // We do not call .get() on 'future' so the calling thread remains non-blocking.
    }
}