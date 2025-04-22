package com.example.diary.web;

import com.example.diary.domain.AppUser;
import com.example.diary.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.validation.Valid;

@Controller
public class RegistrationController {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("appUser", new AppUser());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid AppUser appUser,
            BindingResult bindingResult,
            Model model) {

        // Tarkista virheet
        if (bindingResult.hasErrors()) {
            return "register";
        }

        // Tarkista onko käyttäjätunnus varattu
        if (appUserRepository.findByUsername(appUser.getUsername()).isPresent()) {
            model.addAttribute("usernameError", "Käyttäjätunnus on jo varattu");
            return "register";
        }

        // Tarkista onko sähköposti varattu
        if (appUserRepository.findByEmail(appUser.getEmail()).isPresent()) {
            model.addAttribute("emailError", "Sähköposti on jo rekisteröity");
            return "register";
        }

        // Hashaa salasana ja tallenna käyttäjä
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        appUserRepository.save(appUser);

        return "redirect:/login?registered";
    }
}