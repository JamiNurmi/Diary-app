package com.example.diary.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.diary.domain.Entry;
import com.example.diary.repository.EntryRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/entries")
public class EntryController {
    @Autowired
    private EntryRepository entryRepository;

    // Kaikki merkinnät
    @GetMapping
    public List<Entry> getAllEntries() {
        return entryRepository.findAll();
    }

    // Uusi merkintä
    @PostMapping
    public Entry createEntry(@RequestBody Entry entry) {
        return entryRepository.save(entry);
    }

    // Poista merkintä
    @DeleteMapping("/{id}")
    public void deleteEntry(@PathVariable Long id) {
        entryRepository.deleteById(id);
    }

    @GetMapping("/searchByDate")
    public List<Entry> searchByDate(
            @RequestParam("start") String start,
            @RequestParam("end") String end) {

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        LocalDateTime startDate = LocalDateTime.parse(start, formatter);
        LocalDateTime endDate = LocalDateTime.parse(end, formatter);

        return entryRepository.findByCreatedAtBetween(startDate, endDate);
    }
}