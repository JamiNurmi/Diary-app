package com.example.diary.repository;

import java.util.List;
import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.diary.domain.Entry;

public interface EntryRepository extends JpaRepository<Entry, Long> {

    @Query("SELECT e FROM Entry e ORDER BY e.createdAt DESC")
    List<Entry> findAllByOrderByCreatedAtDesc();

    List<Entry> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

}