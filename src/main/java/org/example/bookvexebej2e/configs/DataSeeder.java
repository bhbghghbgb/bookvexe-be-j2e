package org.example.bookvexebej2e.configs;

import lombok.extern.slf4j.Slf4j;
import org.example.bookvexebej2e.models.db.RoleDbModel;
import org.example.bookvexebej2e.models.db.RoleUserDbModel;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.repositories.role.RoleRepository;
import org.example.bookvexebej2e.repositories.role.RoleUserRepository;
import org.example.bookvexebej2e.repositories.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

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
}