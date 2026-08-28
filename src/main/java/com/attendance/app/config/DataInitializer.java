package com.attendance.app.config;

import com.attendance.app.model.Role;
import com.attendance.app.model.User;
import com.attendance.app.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner seedDatabase(UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            if (!userRepository.existsByUsername("admin")) {
                userRepository.save(new User("admin", encoder.encode("admin123"),
                        "System Administrator", "admin@attendance.local", Role.ADMIN));
            }
            if (!userRepository.existsByUsername("jdoe")) {
                userRepository.save(new User("jdoe", encoder.encode("student123"),
                        "John Doe", "jdoe@attendance.local", Role.STUDENT));
            }
            if (!userRepository.existsByUsername("asmith")) {
                userRepository.save(new User("asmith", encoder.encode("faculty123"),
                        "Alice Smith", "asmith@attendance.local", Role.FACULTY));
            }
        };
    }
}
