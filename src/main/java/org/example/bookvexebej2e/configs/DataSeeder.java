package org.example.bookvexebej2e.configs;

import lombok.extern.slf4j.Slf4j;
import org.example.bookvexebej2e.models.db.NotificationTypeDbModel;
import org.example.bookvexebej2e.models.db.RoleDbModel;
import org.example.bookvexebej2e.models.db.RoleUserDbModel;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.repositories.notification.NotificationTypeRepository;
import org.example.bookvexebej2e.repositories.role.RoleRepository;
import org.example.bookvexebej2e.repositories.role.RoleUserRepository;
import org.example.bookvexebej2e.repositories.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Configuration
@Slf4j
public class DataSeeder {

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin";
    private static final String ADMIN_ROLE_CODE = "ADMIN";

    private final RoleRepository roleRepository;
    private final RoleUserRepository roleUserRepository;

    public DataSeeder(RoleRepository roleRepository, RoleUserRepository roleUserRepository) {
        this.roleRepository = roleRepository;
        this.roleUserRepository = roleUserRepository;
    }

    @Bean
    @Transactional
    public CommandLineRunner initDefaultAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {

        return args -> {

            // Ensure the ADMIN role exists
            RoleDbModel adminRole = roleRepository.findByCode(ADMIN_ROLE_CODE)
                .orElseGet(() -> {
                    log.info("ADMIN role not found. Creating default ADMIN role.");
                    RoleDbModel role = new RoleDbModel();
                    role.setCode(ADMIN_ROLE_CODE);
                    role.setName("Administrator");
                    role.setDescription("Full system access and management privileges.");
                    return roleRepository.save(role);
                });

            // Check if the default user already exists
            Optional<UserDbModel> existingUser = userRepository.findByUsernameAndNotDeleted(DEFAULT_USERNAME);

            if (existingUser.isEmpty()) {
                log.info("No 'admin' user found. Creating default administrator account.");


                UserDbModel adminUser = new UserDbModel();
                adminUser.setUsername(DEFAULT_USERNAME);
                adminUser.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
                adminUser.setIsAdmin(true);

                // Save the user first to get an ID
                UserDbModel savedUser = userRepository.save(adminUser);

                // Assign the ADMIN role
                try {
                    // Check if the association already exists
                    Optional<RoleUserDbModel> existingAssociation = roleUserRepository.findByUserIdAndRoleId(
                        savedUser.getId(), adminRole.getId());
                    if (existingAssociation.isEmpty()) {
                        RoleUserDbModel roleUser = new RoleUserDbModel();
                        roleUser.setUser(savedUser);
                        roleUser.setRole(adminRole);
                        roleUserRepository.save(roleUser);

                        log.info(
                            "Default administrator account created successfully: " + DEFAULT_USERNAME + "/" + DEFAULT_PASSWORD);
                    } else {
                        log.info("Default user already has ADMIN role association. Skipping assignment.");
                    }
                } catch (Exception e) {
                    log.error("Failed to assign ADMIN role to default user: {}", e.getMessage());
                }
            } else {
                log.info("Default 'admin' user already exists. Skipping creation.");
            }
        };
    }

    @Bean
    @Transactional
    public CommandLineRunner initNotificationTypes(NotificationTypeRepository notificationTypeRepository) {
        return args -> {
            log.info("Seeding notification types...");

            // List of known notification type codes with name and description
            List<Object[]> notificationTypes = Arrays.asList(
                // Format: [code, name, description]
                new Object[]{"TYPE_BOOKING_CREATED", "Booking Created", "Notification when a new booking is created"},
                new Object[]{"TYPE_BOOKING_CONFIRMED", "Booking Confirmed", "Notification when a booking is confirmed"},
                new Object[]{"TYPE_BOOKING_CANCELLED", "Booking Cancelled", "Notification when a booking is cancelled"},
                new Object[]{"TYPE_BOOKING_COMPLETED", "Booking Completed", "Notification when a trip is completed"},
                new Object[]{"TYPE_BOOKING_AWAIT_PAYMENT", "Awaiting Payment", "Notification when booking is waiting for payment"},
                new Object[]{"TYPE_BOOKING_AWAIT_GO", "Awaiting Departure", "Notification when booking is confirmed and waiting for departure"},
                new Object[]{"TYPE_BOOKING_DEPARTING", "Trip Departing", "Notification when trip is departing"},
                new Object[]{"TYPE_PAYMENT_SUCCESS", "Payment Successful", "Notification when payment is successful"},
                new Object[]{"TYPE_PAYMENT_FAILED", "Payment Failed", "Notification when payment fails"},
                new Object[]{"TYPE_PAYMENT_PENDING", "Payment Pending", "Notification when payment is pending"},
                new Object[]{"TRIP_DEPARTURE_REMINDER", "Departure Reminder", "Reminder notification before trip departure"},
                new Object[]{"TRIP_SCHEDULE_CHANGE", "Schedule Change", "Notification when trip schedule changes"},
                new Object[]{"TRIP_CANCELLED", "Trip Cancelled", "Notification when a trip is cancelled"},
                new Object[]{"SYSTEM_ANNOUNCEMENT", "System Announcement", "General system announcements"},
                new Object[]{"PROMOTION", "Promotion", "Promotional notifications"},
                new Object[]{"NEWS_UPDATE", "News Update", "News and updates"},
                new Object[]{"TEST_NOTIFICATION", "Test Notification", "For testing purposes"},
                new Object[]{"TEST", "Test Notification 0", "For testing purposes"},
                new Object[]{"TYPE_DEPARTURE_REMINDER", "Departure Reminder 0", "Reminder for upcoming trips"}
            );

            int createdCount = 0;
            int updatedCount = 0;

            for (Object[] typeData : notificationTypes) {
                String code = (String) typeData[0];
                String name = (String) typeData[1];
                String description = (String) typeData[2];

                Optional<NotificationTypeDbModel> existingType = notificationTypeRepository.findByCode(code);

                if (existingType.isPresent()) {
                    NotificationTypeDbModel existing = existingType.get();
                    boolean needsUpdate = !existing.getName().equals(name) ||
                        !existing.getDescription().equals(description);

                    if (needsUpdate) {
                        existing.setName(name);
                        existing.setDescription(description);
                        notificationTypeRepository.save(existing);
                        updatedCount++;
                    }
                } else {
                    NotificationTypeDbModel newType = new NotificationTypeDbModel();
                    newType.setCode(code);
                    newType.setName(name);
                    newType.setDescription(description);
//                    newType.setIsActive(true);

                    notificationTypeRepository.save(newType);
                    createdCount++;
                }
            }

            log.info("Notification type seeding completed. Created: {}, Updated: {}, Total: {}",
                createdCount, updatedCount, notificationTypes.size());
        };
    }
}