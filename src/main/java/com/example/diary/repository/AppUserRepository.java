package com.example.diary.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.diary.domain.AppUser;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);

    Optional<AppUser> findByEmail(String email);
}