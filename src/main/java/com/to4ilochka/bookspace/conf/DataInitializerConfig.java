package com.to4ilochka.bookspace.conf;

import com.to4ilochka.bookspace.model.User;
import com.to4ilochka.bookspace.model.enums.Role;
import com.to4ilochka.bookspace.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializerConfig {
    @Value("${app.root.admin.email}")
    private String adminEmail;
    @Value("${app.root.admin.password}")
    private String adminPassword;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initRootAdmin() {
        return args -> {
            if (!userRepository.existsByRoles(Role.ROLE_ADMIN)) {
                User rootAdmin = new User();
                rootAdmin.setEmail(adminEmail);
                rootAdmin.setName("System Admin");
                rootAdmin.setPassword(passwordEncoder.encode(adminPassword));
                rootAdmin.getRoles().add(Role.ROLE_ADMIN);
                userRepository.save(rootAdmin);
            }
        };
    }
}