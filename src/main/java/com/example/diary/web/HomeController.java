package com.example.diary.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.diary.domain.Entry;
import com.example.diary.repository.EntryRepository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private EntryRepository entryRepository;

    @GetMapping
    public String home(@RequestParam(name = "order", defaultValue = "desc") String order, Model model) {
        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        List<Entry> entries = entryRepository.findAll(Sort.by(direction, "createdAt"));
        model.addAttribute("entries", entries);
        model.addAttribute("order", order);
        return "index"; // Thymeleaf-sivun nimi (esim. index.html)
    }

    @PostMapping("/add")
    public String addEntry(@RequestParam String title,
            @RequestParam String content) {
        Entry newEntry = new Entry(title, content);
        entryRepository.save(newEntry);
        return "redirect:/";
    }

    @PostMapping("/update")
    public String updateEntry(@RequestParam Long id,
            @RequestParam String title,
            @RequestParam String content) {
        entryRepository.findById(id).ifPresent(entry -> {
            entry.setTitle(title);
            entry.setContent(content);
            entry.setUpdatedAt(LocalDateTime.now());
            entryRepository.save(entry);
        });
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String deleteEntry(@PathVariable Long id) {
        entryRepository.deleteById(id);
        return "redirect:/";
    }
}