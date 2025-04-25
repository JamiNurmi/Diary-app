package com.example.diary;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.diary.domain.AppUser;
import com.example.diary.repository.AppUserRepository;
import com.example.diary.repository.EntryRepository;
import com.example.diary.domain.Entry;

@Configuration
public class DataLoaderConfig {

    @Bean
    CommandLineRunner loadData(EntryRepository entryRepository) {
        return args -> {
            if (entryRepository.count() == 0) {
                entryRepository.save(new Entry("Fake data 1", "Aloitin päiväkirjan pitämisen tänään."));
                entryRepository.save(new Entry("Fake data 2", "Opin lisää Javaa ja Spring Bootia."));
                entryRepository.save(new Entry("Fake data 3", "Join kahvia ja luin kirjaa koko päivän."));
            }
        };
    }

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