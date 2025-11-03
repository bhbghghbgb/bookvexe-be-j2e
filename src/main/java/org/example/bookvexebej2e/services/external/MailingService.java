package org.example.bookvexebej2e.services.external;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailingService {

    private final UserRepository userRepository;
    private final JavaMailSender emailSender;

    @Value("${app.email.sender}")
    private String senderEmail;

    /**
     * Lower-level function to send an email using Spring's JavaMailSender (configured for SMTP).
     */
    public void sendEmail(String toEmail, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderEmail);
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);

            emailSender.send(message);
            log.info("Successfully sent email via SMTP. To: {}, Subject: {}", toEmail, subject);
        } catch (Exception e) {
            log.error("SMTP Mail Error for {}. Subject: {}. Error: {}", toEmail, subject, e.getMessage(), e);
            log.warn("Email Sent via Placeholder Log (due to failure): To: {}, Subject: {}", toEmail, subject);
        }
    }

    /**
     * High-level function to send an email to a UserDbModel (Employee/Customer).
     */
    public void sendEmailToUser(UUID userId, String subject, String body) {
        userRepository.findById(userId).ifPresentOrElse(user -> {
            String email = getUserEmail(user);
            if (email != null) {
                sendEmail(email, subject, body);
            } else {
                log.warn("User {} has no associated email address (Customer/Employee) for sending.", userId);
            }
        }, () -> {
            log.warn("Attempted to send email to non-existent user ID: {}", userId);
        });
    }

    private String getUserEmail(UserDbModel user) {
        if (user.getCustomer() != null) {
            return user.getCustomer().getEmail();
        }
        if (user.getEmployee() != null) {
            return user.getEmployee().getEmail();
        }
        return null;
    }
}