package com.bookvexe.mailservice.batch;

import com.bookvexe.mailservice.dto.MailKafkaDTO;
import jakarta.mail.internet.MimeMessage;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.transaction.PlatformTransactionManager;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Configuration
@RequiredArgsConstructor
public class SpringBatchConfig {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final List<MailKafkaDTO> mailQueue = new ArrayList<>(); // Shared queue

    @Value("${mail.dry-run:false}")
    private boolean dryRun;

    @Bean
    public Job sendMailJob(JobRepository jobRepository, Step sendMailStep) {
        return new JobBuilder("sendMailJob", jobRepository).flow(sendMailStep)
            .end()
            .build();
    }

    @Bean
    public Step sendMailStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("sendMailStep", jobRepository).<MailKafkaDTO, MimeMessage>chunk(10,
                transactionManager) // Process 10 emails per batch
            .reader(mailItemReader())
            .processor(mailItemProcessor())
            .writer(mailItemWriter())
            .build();
    }

    @Bean
    public ItemReader<MailKafkaDTO> mailItemReader() {
        return () -> {
            synchronized (mailQueue) {
                if (!mailQueue.isEmpty()) {
                    return mailQueue.remove(0);
                }
            }
            return null; // End of reading
        };
    }

    @Bean
    public ItemProcessor<MailKafkaDTO, MimeMessage> mailItemProcessor() {
        return item -> {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            Context context = new Context();
            context.setVariables(item.getTemplateModel());
            String htmlContent = templateEngine.process(item.getTemplateName(), context);

            helper.setTo(item.getTo());
            helper.setSubject(item.getSubject());
            helper.setText(htmlContent, true);

            return mimeMessage;
        };
    }

    @Bean
    public ItemWriter<MimeMessage> mailItemWriter() {
        return items -> {
            for (MimeMessage message : items) {
                if (dryRun) {
                    log.info("[Dry-Run] Skipping send: {}", message.getSubject());
                } else {
                    mailSender.send(message);
                    log.info("Sent email: {}", message.getSubject());
                }
            }
        };
    }

    @Bean
    public List<MailKafkaDTO> mailQueue() {
        return mailQueue;
    }
}
