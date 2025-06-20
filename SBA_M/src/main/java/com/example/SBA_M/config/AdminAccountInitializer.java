package com.example.SBA_M.config;

import com.example.SBA_M.entity.commands.Account;
import com.example.SBA_M.entity.commands.Role;
import com.example.SBA_M.repository.commands.AccountRepository;
import com.example.SBA_M.repository.commands.RoleRepository;
import com.example.SBA_M.utils.AccountStatus;
import com.example.SBA_M.utils.Gender;
import com.example.SBA_M.utils.RoleName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Configuration
public class AdminAccountInitializer {

    private static final Logger logger = LoggerFactory.getLogger(AdminAccountInitializer.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.admin.username:admin}")
    private String adminUsername;

    @Value("${app.admin.email:admin@sba-m.com}")
    private String adminEmail;

    @Value("${app.admin.password:Admin@123}")
    private String adminPassword;

    @Value("${app.admin.full-name:System Administrator}")
    private String adminFullName;

    @Value("${app.admin.phone:+1234567890}")
    private String adminPhone;

    @Value("${app.admin.create-on-startup:true}")
    private boolean createAdminOnStartup;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initializeAdminAccount() {
        if (!createAdminOnStartup) {
            logger.info("Admin account creation on startup is disabled");
            return;
        }

        try {
            // Check if any accounts exist
            long accountCount = accountRepository.count();

            if (accountCount == 0) {
                logger.info("No accounts found in database. Creating default admin account...");
                createDefaultAdminAccount();
            } else {
                logger.info("Database already contains {} account(s). Skipping admin account creation.", accountCount);

                // Optional: Check if admin user specifically exists
                Optional<Account> existingAdmin = accountRepository.findByUsername(adminUsername);
                if (existingAdmin.isEmpty()) {
                    logger.info("Admin account '{}' not found. Creating admin account...", adminUsername);
                    createDefaultAdminAccount();
                } else {
                    logger.info("Admin account '{}' already exists.", adminUsername);
                }
            }

        } catch (Exception e) {
            logger.error("Error occurred while initializing admin account: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to initialize admin account", e);
        }
    }

    private void createDefaultAdminAccount() {
        try {
            // Create or get ADMIN role
            Role adminRole = createOrGetAdminRole();

            // Create admin account
            Account adminAccount = new Account();
            adminAccount.setUsername(adminUsername);
            adminAccount.setEmail(adminEmail);
            adminAccount.setPassword(passwordEncoder.encode(adminPassword));
            adminAccount.setFullName(adminFullName);
            adminAccount.setPhone(adminPhone);
            adminAccount.setStatus(AccountStatus.ACTIVE);
            adminAccount.setGender(Gender.OTHER); // Set default gender or make it configurable
            adminAccount.setDob(LocalDate.of(1990, 1, 1)); // Set default DOB or make it configurable
            adminAccount.setLoginCount(0);
            adminAccount.setIsDeleted(false);
            adminAccount.setCreatedBy("SYSTEM");

            // Set roles
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            adminAccount.setRoles(roles);

            // Save admin account
            Account savedAccount = accountRepository.save(adminAccount);

            logger.info("Successfully created admin account:");
            logger.info("  Username: {}", savedAccount.getUsername());
            logger.info("  Email: {}", savedAccount.getEmail());
            logger.info("  Full Name: {}", savedAccount.getFullName());
            logger.info("  Status: {}", savedAccount.getStatus());
            logger.info("  Roles: {}", savedAccount.getRoles().stream().map(role -> role.getName().toString()).toList());

        } catch (Exception e) {
            logger.error("Failed to create admin account: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create admin account", e);
        }
    }

    private Role createOrGetAdminRole() {
        Optional<Role> existingRole = roleRepository.findByName(RoleName.ADMIN);

        if (existingRole.isPresent()) {
            logger.info("ADMIN role already exists");
            return existingRole.get();
        }

        // Create ADMIN role if it doesn't exist
        Role adminRole = new Role();
        adminRole.setName(RoleName.ADMIN);
        adminRole.setDescription("System Administrator with full access to all features");

        Role savedRole = roleRepository.save(adminRole);
        logger.info("Created ADMIN role: {}", savedRole.getName());
        return savedRole;
    }
}