package com.ga.thirdgear.seeder;

import com.ga.thirdgear.enums.UserRole;
import com.ga.thirdgear.enums.UserStatus;
import com.ga.thirdgear.model.User;
import com.ga.thirdgear.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class UserSeeder {

    private static final Logger logger = LoggerFactory.getLogger(UserSeeder.class);

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserRepository(UserRepository userRepository) { this.userRepository = userRepository; }
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) { this.passwordEncoder = passwordEncoder; }

    public void seed() {
        if (userRepository.count() > 0) return;

        userRepository.save(User.builder()
                .firstName("Admin").lastName("ThirdGear")
                .email("admin@thirdgear.com")
                .password(passwordEncoder.encode("Admin@123"))
                .role(UserRole.ROLE_ADMIN).status(UserStatus.ACTIVE)
                .emailVerified(true).build());

        userRepository.save(User.builder()
                .firstName("Alice").lastName("Smith")
                .email("alice@test.com")
                .password(passwordEncoder.encode("Alice@123"))
                .role(UserRole.ROLE_USER).status(UserStatus.ACTIVE)
                .emailVerified(true).build());

        userRepository.save(User.builder()
                .firstName("Bob").lastName("Johnson")
                .email("bob@test.com")
                .password(passwordEncoder.encode("Bob@123"))
                .role(UserRole.ROLE_USER).status(UserStatus.ACTIVE)
                .emailVerified(true).build());

        logger.info("✅ Users seeded!");
    }
}