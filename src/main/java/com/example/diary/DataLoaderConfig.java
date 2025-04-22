package com.example.diary;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.diary.domain.AppUser;
import com.example.diary.repository.AppUserRepository;

@Configuration
public class DataLoaderConfig {

    @Bean
    public CommandLineRunner dataLoader(AppUserRepository repository, PasswordEncoder encoder) {
        return args -> {
            // Tarkista onko käyttäjiä jo tietokannassa
            if (repository.findByUsername("user").isEmpty()) {
                AppUser user = new AppUser();
                user.setUsername("user");
                user.setPassword(encoder.encode("user123")); // Vahvempi salasana
                user.setEmail("user@example.com");
                user.setRoles(Set.of("USER")); // Aseta rooli
                repository.save(user);
            }

            if (repository.findByUsername("admin").isEmpty()) {
                AppUser admin = new AppUser();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("admin123")); // Vahvempi salasana
                admin.setEmail("admin@example.com");
                admin.setRoles(Set.of("ADMIN", "USER")); // Useampi rooli
                repository.save(admin);
            }
        };
    }
}