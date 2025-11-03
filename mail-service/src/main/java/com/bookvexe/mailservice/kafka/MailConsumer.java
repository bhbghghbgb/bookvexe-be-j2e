package com.bookvexe.mailservice.kafka;

import com.bookvexe.mailservice.dto.MailKafkaDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailConsumer {

    private final JobLauncher jobLauncher;
    private final Job sendMailJob;
    private final List<MailKafkaDTO> mailQueue = new ArrayList<>();

    @KafkaListener(topics = "mail-topic", groupId = "mail_group")
    public void consume(MailKafkaDTO kafkaDTO) {
        log.info("Received mail request from Kafka: {}", kafkaDTO);
        synchronized (mailQueue) {
            mailQueue.add(kafkaDTO);
        }
    }

    // Chạy định kỳ để xử lý queue và khởi chạy Batch Job
    // (Trong thực tế, có thể dùng @Scheduled hoặc một cơ chế trigger khác)
    public void triggerMailBatch() {
        if (!mailQueue.isEmpty()) {
            try {
                jobLauncher.run(sendMailJob, new JobParametersBuilder()
                        .addLong("time", System.currentTimeMillis())
                        .toJobParameters());
            } catch (Exception e) {
                log.error("Error launching mail batch job", e);
            }
        }
    }
}

